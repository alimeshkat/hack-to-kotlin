# An introduction to Coroutines

Kotlin coroutines can be viewed as lightweight threads. You can create thousands of them without running out of memory.
Coroutines can be used out of the box because they are part of the Kotlin standard library. By adding the `suspend`
modifier to your function, the Kotlin compiler understands that it won't return directly, but it will suspend for some
time to complete an I/O operation or some heavy CPU computation. Simply put, marking a function with the suspend
modifier will run the code in that function in a coroutine. Only a suspend function or a coroutine scope can call other
suspend functions.

# TL;DR

"Coroutines are part of the Kotlin standard library, so they can be used out-of-the-box! By marking a function with
the `suspend` modifier, the compiler understands that the function
can contain one or more suspension points (i.e., calls to other `suspend` functions), and so it will not return right
away
but suspend the coroutine until the result is there.

That's also why a suspend function can only be called within the scope of another coroutine or suspend function because
it can suspend a coroutine, not a thread. The implementation of the coroutine is achieved with the `Continuation Passing
Style` (CPS) transformation transparently by the compiler.

A simple form of CPS is a callback, and so one can view coroutines as callbacks. The compiler transforms a suspended
function into a state machine that stores the current state in a Continuation (callback) object at each suspension
point. This
Continuation object is passed down to each suspend function. When the function is done, the Continuation
object is called to resume with the rest of the code.

A state machine implementation is more efficient than a callback implementation because it reuses the `Continuation`
object that keeps track of the state, between the suspension points, instead of creating objects for each lambda like it
happens with regular CPS . The CPS transformation abstracts away
the complexity of the CPS so that you can write direct style code, which is much easier to reason with."

Continue reading about Coroutines, or go to the next section to read about [Coroutines library](CoroutineLibrary.md).

--- 

## suspend

Let's take a look at a simple program that prints `Hello`, `Coroutine` with a delay of one second and refactor it so
that we don't have to use the ugly Thread.sleep() method but instead use coroutines.

```Kotlin
fun main() {
    println("Hello")
    Thread.sleep(1000)
    println("Coroutine")

}
```

Now change it to coroutines by adding the `suspend` modifier and replace the `Thread.sleep()` with the suspend
function `delay()` which suspends the current coroutine for some time.

```Kotlin
suspend fun main() {
    println("Hello")
    delay(1000)
    println("Coroutine")

}
```

Now we are done!
*Please note that even though the code in the main method is executed in a coroutine*, it doesn't mean it
is non-blocking. The `delay()` function pauses the program because it is called in the main coroutine, which runs on a
single thread. To perform tasks in parallel with coroutines, we need to use constructs like coroutine builders such as
`launch`, `async/await`, and `dispatchers`. We will cover these concepts later on it following section about coroutine
builders.
For now, we want to explain how coroutines work internally to remove the "magic" and provide a better understanding of
how they function. For example, we can
answer the following question: How is it that we only need to add a suspend modifier to a function, and inside that
function, we can suspend (as we did with `delay()`) and resume at any point? To answer this question, we must understand
what Continuation Passing Style means in Kotlin.

## Continuation & Continuation Passing Style

`Continuation Passing Style` (CPS) is a technical term used to describe a programming style that uses callbacks to pass
values and control flow between functions. It's often used in asynchronous programming to handle tasks that take time to
complete, such as fetching data from a remote server or reading a large file.

To understand CPS, let's compare it to the more common programming style, called direct or linear style, where a
function directly returns a value to its caller. In CPS, instead of returning the result directly, a function passes the
result to a callback function, which is responsible for continuing the program execution.

```Kotlin
fun sendMailToUser(user: User, message: String) {
    val contactDetail = getContactDetail(user)
    val result = sendMail(contactDetail.email, message)
    processMailResult(result)
}
```

For example, in our simple program, the first action is to call the `getContactDetail()` function to fetch the contact
details from the userService. We need the result of this function to continue, but we can't block the program execution
while we wait for it. Instead, we provide a callback function that will be called when the result is available, and we
can continue the program execution in this callback function. The same approach is also used for sending the email.

```Kotlin
fun sendMailToUser(user: User, message: String) {
    getContactDetail(user) { contactDetail ->
        sendMail(contactDetail.email, message) { result ->
            processMailResult(result)
        }
    }
}
```

And finally the CPS with coroutines:

```Kotlin
suspend fun sendMailToUser(id: User, message: String) {
    val contactDetail = getContactDetail(id) //suspend function
    val result = sendMail(contactDetail.email, message) //suspend function
    processMailResult(result)
}
```

It's interesting to see that it's written in the direct style, but underwater it uses CPS. Let's see how does this
works.

During compilation, the signature of the suspend function is modified to include a `Continuation` instance, which serves
as a generic callback. If you decompile the `sendMailToUser()` function, you can see that this callback is added to the
code.

```Java
public static final Object sendMailToUser(User id,String message,Continuation var2){
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

The Continuation interface consists of a context, which holds the coroutine-local information, and the function
resumeWith(), which is called with the result of the last suspension point and resumes the coroutine execution.

If we take the first example code that used the `delay()` function from
the [`kotlinx-coroutines`](https://github.com/Kotlin/kotlinx.coroutines)
library and replace it with one of our own simplified implementations, it would look like this:

```Kotlin
val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

suspend fun main() {

    val result = suspendCoroutine<String> { continuation ->  //gets the current continuation
        executor.schedule( //Schedules a thread to call resumeWith(), with the result, after 1 sec 
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

So far we learned the way Kotlin supports Continues Passing Style (CPS) to support writing coroutines.
The CPS code is generated by the compiler, it transforms a `suspend` functions into a function that
accepts a callback object, a `Continuation`. Now let's see how this `Continuation` is used within the scope of a suspend
function.

## State machine

The CPS transformation at compile time doesn't generate callbacks, but a more efficient means to pass the continuation
as a parameter, a state machine.

Let us look at our suspended `sendMailToUser` function and try to describe the generated state machine for it in
*pseudo*
code.

1) A state machine (sm) is created to keep the current execution state
2) A switch case is created for each action and its following continuation represented by a label. At `case 0`, the
   `getContactDetail()` is called, and because we are dealing with a suspended function that resumes somewhere in time,
   the
   parameters are stored in the sm for the next time use, together with the new label (next action). The SM is passed to
   the `getContactDetail()`, so when it's finished executing, it would call resumeWith() (step 3) function on the sm to
   continue.
3) The `resumeWith` implementation we have provided in sm will just call the `sendMailToUser` with the same sm
4) Note that the label value was set to 1 in `case 0`, so now `case 1` gets executed. The previous state will get
   restored to be used to call
   `sendMailToUser`. Again, the label gets updated, and when `sendMailToUser` is
   finished the result wil get stored in the sm for the next continuation to use. This will continue till every case has
   been run through.

```Kotlin

suspend fun sendMailToUser(user: User, message: String, cont: Continuation) {
    //1
    val sm = cont as ThisSM ?: object : ThisSM {
        var label = 0 //current state ot the state machine

        //3
        fun resumeWith(...) { // implemented resumeWith 
            sendMailToUser(null, null, this)
        }
    }
    switch(sm.label) {
        //2
        case 0:
        sm.message = message
        sm.label = 1
        val result = getContactDetail(user, sm)
        if (result == COROUTINE_SUSPENDED) return COROUTINE_SUSPENDED //COROUTINE_SUSPENDED is return when no result is returned by the suspend function
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

