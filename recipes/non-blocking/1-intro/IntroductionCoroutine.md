# TL;DR

With Kotlin coroutines you can write non-blocking code. Coroutines is part of the Kotlin standard library, so it can be used out-of-the-box!
By marking a function with the `suspend` modifier the compiler understands that the function can contain one or more
suspension points (i.e. calls to other suspend functions), and so, it will not return right-a-way but suspends the
coroutine until the result is there.
That's also why suspend function can only be called within the scope of another coroutine or suspend function, because, it *can* suspend a coroutine not a thread.
The implementation of the coroutine is achieved with the `Continuation Passing Style` (CPS) transformation transparently by the
compiler.
A simple form of CPS is a callback, and so, one can view Coroutines a callbacks.
The compiler transforms a suspended function in to a *state machine* that stores the current state in a `Continuation` object at each suspension point.
This `Continuation` (callback) object is passed down to each suspend function is done, it is called to resume with the rest of the code.
A statemachine implementation is more efficient than a callback implementation, because it reuses the Continuation object that keeps
track of the state instead of creating objects for each lambda.
The CPS transformation abstract away the complexity of the CPS, so you can write direct style code which is much easier
to reason with.

Continue reading about Coroutines, or go to the next section to read about [Coroutines library](CoroutineLibrary.md).

--- 

# An introduction to Coroutines

Kotlin coroutine can be viewed as a light-weight thread, you can create thousands of them without running out of memory.
Coroutines can be used out of the box because it is part of the Kotlin's standard library.
By adding a modifier called `suspend` to your function the Kotlin compile understands that it won't return directly, but
it will *suspend* for some time to complete an IO operations or some heavy cpu computation.
Simply said, marking a function with `suspend` modifier, will run the code in that function in a coroutine.
Only form a suspend function, or a within a coroutine scope, other suspend functions can be called.

## suspend

Let's have a look at a simple program that prints `Hello` `Coroutine` with a delay of 1 second, and refactor it, so we don't have to use ugly `Thread.sleep()` but coroutines.

```Kotlin
fun main(){
    println("Hello")
    Thread.sleep(1000)
    println("Coroutine")

}
```
Now change it to coroutines by adding the `suspend` modifier and replace the `Thread.sleep()` with the suspend function `delay()` which suspends the current coroutine for some time.

```Kotlin
suspend fun main(){
    println("Hello")
    delay(1000)
    println("Coroutine")

}
```

Now we are done! Do note, even though the code in the main method is executed in a coroutine, it doesn't mean it is non-blocking: the `delay`
function pauzes the program because it is called in the main coroutine, which runs on single thread.
To do things in parallel with coroutines, we need to use constructs like coroutine builders such as `launch` and `async/await` and `dispatchers`, we will cover that later on.
For now, we want to explain how coroutines work internally to take away the magic.
So, for example how we like to answer the following question:
How come that we only have to add a `suspend` modifier to a function, and inside that function we can suspend (as we used delay() before) and resume at any point?
To answer the question above, we will have to understand what `Continuation Passing Style` means in Kotlin. 

## Continuation & Continuation Passing Style

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


If we take the first example code that used the `delay()` function which is from
the [`kotlinx-coroutines`](https://github.com/Kotlin/kotlinx.coroutines) and replace it with one of our own simplified implementation, it would look like this

```Kotlin
val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

suspend fun main() {

    val result = suspendCoroutine<String> { continuation ->  //gets the current continuation
        executor.schedule( //Scheduled a thread to call resume after 1 sec 
            {
               continuation.resumeWith(Result.success("Hello")) // The result to be returned
            },
            1000, TimeUnit.MILLISECONDS
        ) 
    }
   
    println(result) //Print "Hello"
    println("Coroutine")
}

```

So now we know what how a suspend function is transformed to support CPS, lets take a look at the implementation of Kotlin Coroutines.

## State machine

The CPS transformation at compile time doesn't generate callbacks, but a more efficient means to pass the continuation
as a parameter, a state machine.

Let us look at our suspended `sendMailToUser` function and try to describe the generated state machine for it in *sudo*
code.

1) a state machine (sm) is created to keep the current execution state
2) a switch case is created for each action and its following continuation represented by a label.
   At `case0` the `getContactDetail` is called, and because we are dealing with a suspended function that resumes somewhere in time, the parameters
   are stored in sm for the next time use, together with the new label (next action). The sm is
   passed to the `getContactDetail` so when it's finished executing, it would call resumeWith (3) function on the sm to
   continue
3) The `resumeWith` implementation we have provided in sm will just call the `sendMailToUser` with the same sm
4) Note that the label value was set to 1 in `case 0`, so now `case 1` gets executed. The previous state will get
   restored to be used to call
   `sendMailToUser`. Again the label gets updated and when `sendMailToUser` is
   finished the result wil get stored in the sm for the next continuation to use. This will continue till every case has
   been run through.

```Kotlin

import kotlin.coroutines.Continuation

suspend fun sendMailToUser(user: User, message: String, cont: Continuation) {
  //1
  val sm = cont as ThisSM ?: object : ThisSM {
      var label = 0 //current state ot the state machine
      //3
      fun resumeWith(...){ // implemented resumeWith 
         sendMailToUser(null, null, this)
      }
  }
  switch(sm.label){
     //2
     case 0: 
        sm.message = message
        sm.label = 1
        val result = getContactDetail(user, sm)
        if (result == COROUTINE_SUSPENDED) return COROUTINE_SUSPENDED
    //4
     case 1:
        val message = sm.message
        val contactDetails = sm.result as ContactDetail
        sm.label = 2
        val result = sendMailToUser(contactDetails.email, message, sm)
        if (result == COROUTINE_SUSPENDED) return COROUTINE_SUSPENDED
     ...
  }
}
```

# Next, Coroutine library

Now you know what Kotlin Coroutine is, and how it works out of the box, let's look at the `kotlinx-coroutine` library
which offers a rich api for building coroutines.

[NEXT](CoroutineLibrary.md)

