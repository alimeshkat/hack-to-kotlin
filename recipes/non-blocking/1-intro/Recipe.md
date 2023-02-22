# Intro

It turned out, the `Recipe` service has become populair. But unfortunately under the new
tremendous load (10,000s req/sec), it's not performing that great and sometimes becomes unresponsive. We are now
thinking about doing things differently by refactoring the code once again. 

## Blocking

Why our service is not performing that great? Well one reason is because it's blocking.
The thread that handles the call to the Recipe service gets blocked when waiting on the result from the recipes'
database, i.e. blocking `I/O` operation.
Because threads are expensive in terms of CPU and memory usage you cannot create unlimited amount of them, especially in
containerized environments where
resources are restricted. So we have a pool of them and reuse them whenever we can. But, In peak moments, when all
available threads are running or blocked can lead to unresponsiveness of our service.

## Non-Blocking

So, let's make our service `non-blocking`, right?
Because we are familiare with `Spring Boot` framework we want to use the counterpart to `Spring Web`,
the `Spring WebFlux` module which is fully non-blocking.

### Spring WebFlux

In short why we want to use Spring WebFlux:

- It implements the `Reactive Streams` specification which offers a standard for asynchronous stream processing with non-blocking back
  pressure
- It runs on industry standard servers such as Netty, Undertow, and Servlet 3.1+ containers
- It supports Kotlin Coroutines! More about this in the next paragraph

## An introduction to Coroutines

So, what is Kotlin coroutine?
*"A coroutine is an instance of suspendable computation.
It is conceptually similar to a thread,
in the sense that it takes a block of code to run that works concurrently with the rest of the code.
However, a coroutine is not bound to any particular thread.
It may suspend its execution in one thread and resume in another one."*

Kotlin coroutine can be viewed as a light-weight thread (or a callback), you can create thousands of them without heavily impacting the resources.

### suspend

Kotlin has integrated Coroutines on the language level with the `suspend` modifier. By adding the `suspend` modifier to any function you can make it non-blocking! 
Let's have a look at a simple program that prints `Hello` `Coroutine` with a delay of 1 second, and refactor it, so we don't have to use ugly `Thread.sleep()`. 

```Kotlin
fun main(){
    println("Hello")
    Thread.sleep(1000)
    println("Coroutine")

}
```

Simply said, marking a function with `suspend` modifier, will run the code in that function in a coroutine.
Let's do that with the entry point of our simple program, the main method.
Now we can call other suspend functions from it, as suspend functions can only be called from other suspend functions.
Let's do so by replacing the `Thread.sleep()` with the suspend function `delay()` which pauses the current coroutine for some time.

```Kotlin
suspend fun main(){
    println("Hello")
    delay(1000)
    println("Coroutine")

}
```

Now we are done! Even though the code in the main method is executed in a coroutine, it doesn't mean it is non-blocking: the delay 
function call pauzes the program for one-second and blocks the main thread. 
To do things in parallel with coroutines, we need to use constructs like coroutine builders such as `launch` and `async/await` and `dispatchers`, we will cover that later on. 
For now, we want to explain how coroutines work internally to take away the magic.
So, for example how we like to answer the following question:
How come that we only have to add a `suspend` modifier to a function, and inside that function we can suspend (as we used delay() before) and resume at any point?
To answer the question above, we will have to understand what `Continuation Passing Style` means in Kotlin. 

### Continuation & Continuation Passing Style

`Continuation Passing Style` (CPS) is an expensive term to describe callbacks.
Let's explain it by comparing it with regular or direct style of passing action value to its continuation.

Our simple program does the following things:
1) Gets contact details from the userService, assign the result to contactDetail variable
2) Uses the e-mail address from the contactDetail object to create and send the mail
3) At the end, we process the result of the sendMail service.

When writing it down in direct style, for example the first action is the `getContactDetail` call - that's what we need the result from, but we have to wait on it.
The sending the mail and processing the result of it is considered the continuation - what comes after the action.

```Kotlin
fun sendMailToUser(user: User, message: String) {
  val contactDetail = getContactDetail(user)
  val result = sendMail(contactDetail.email, message)
  processMailResult(result)
}
```

In the Continues Passing Style with regular callbacks the continuation is passed as a parameter to the action:

```Kotlin
fun sendMailToUser(user: User, message: String) {
  getContactDetail(user) { contactDetail ->
    sendMail(contactDetail.email, message) { result ->
      processMailResult(result)
    }
  }
}
```

And finally the CPS with coroutines. It's interesting to see that it incorporates both styles we have seen above  
but written in direct style, which is easier to read and understand. But how does it work?

```Kotlin
suspend fun sendMailToUser(id: User, message: String) {
  val contactDetail = getContactDetail(id) //suspend function
  val result = sendMail(contactDetail.email, message) //suspend function
  processMailResult(result)
}
```

At the compile time the suspend function signature is changed, so it accepts an instance of a `Continuation`, which is basically a generic callback.
Decompiling the `sendMailToUser` function code from before, and you will see it's being added. 

```Java
public static final Object sendMailToUser(User id, String message, Continuation var2) {
        ...
}
```
So let's briefly look at the `Continuation` interface, and later on we will discuss how the continuation is used.

```Kotlin
interface Continuation<in T> {
    val context: CoroutineContext
    fun resumeWith(result: Result<T>)
}
```

The Continuation interface consist out of
- a context, which holds the coroutine-local information
- the function `resumeWith()` which is called with a result of the last suspension point and resumes the coroutine execution.

So now we know what how a suspend function is transformed to support CPS, lets take a look at the implementation.

##### State machine 

The CPS transformation at compile time doesn't generate callbacks, but a more efficient state machine.
Let us look at our `sendMailToUser` function and try to describe the state machine generated for it in sudo code.

1) a statemachine (sm) is created to keep the current execution state
2) a switch case is created for each action and its following continuation represented by a label. In `case0` the
   getContactDetail is called, and because we are dealing with a suspended function the parameters that are getting
   passed to the next continuation (message) will get stored in sm together with the new label. The sm is
   passed to the `getContactDetail` so when the suspended function is done, it would call resumeWith (3) function on the sm to continue
3) The resumeWith implementation will just call the sendMailToUser with the same sm
4) Now the label value is set to 1, `case1` gets executed. As the state of the previous action was stored, it can be used to call
   `sendMailToUser`. Again the label gets updated and when `sendMailToUser` is
   finished the result wil get stored in the sm for the next continuation to use. This will continue till every case has been run through

```Kotlin

import kotlin.coroutines.Continuation

suspend fun sendMailToUser(user: User, message: String, cont: Continuation) {
  //1
  val sm = cont as ThisSM ?: object : ThisSM {
      //3
      fun resumeWith(...){
         sendMailToUser(null, null, this)
      }
  }
  switch(sm.label){
     //2
     case 0: 
        sm.message = message
        sm.label = 1
        getContactDetail(user, sm)
     //4
     case 1:
        val message = sm.message
        val contactDetails = sm.result as ContactDetail
        sm.label = 2
        sendMailToUser(contactDetails.email, message, sm)
     ...
  }
}
```

To recap: A coroutine is turned is created from suspend functions and is different from a regular callback.
With the `Continuation Passing Style` transformation a suspended function is turned in to a statemachine which stores
the state at suspension points and restores the state when it resumes. 
It is more efficient than a callback, because it reuses the same object.You can write direct style code which is much easier to reason with.

## Coroutine API (WIP)

A coroutine can be created with the coroutine builders available from
the [`kotlinx-coroutines`](https://github.com/Kotlin/kotlinx.coroutines) library.
When adding the `suspend` key added to a function it means that the coroutine
function a coroutine can be suspended (waiting on a I/O operation) and resumed (when data is available).
A suspending function can only be called from a suspending function or Coroutines.
Keep in mind a coroutine doesn't (usually) block a thread, it starts on one and resumes on others.

So let's take a simple example of a program that runs a Coroutine, suspends it for a second and then continues.

```Kotlin
val executor = Executors.newSingleThreadScheduledExecutor()

suspend fun main() {
    println("hello")
    suspendCoroutine<Unit> {
        executor.schedule(
            { it.resume(Unit) },
            1000, TimeUnit.MILLISECONDS
        )
    }
    println("continuation")
}

```

**Coroutine builder**

The `runBlocking` function is one of the coroutine builders, and it's a blocking one.
It blocks the current thread until its completion. As the documentation states, this function shouldn't run from other
coroutines but merely used as a bridge between blocking and non-blocking code.
We have added in our `main` method so the program completes only when the coroutines are completed.

Let's take a quick look at the functions `runBlocking` and `delay`

```Kotlin

fun main() = runBlocking { //CoroutineScope

    println("Hello?")

    delay(1000) //suspending function

    println("Hellooo? Heloo?")

}

```
