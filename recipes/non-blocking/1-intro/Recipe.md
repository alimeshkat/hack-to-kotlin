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

### Coroutine mechanics

**Suspend & Continuation**

Mark a function with `suspend` modifier, and it will be run in a coroutine.
```Kotlin
suspend fun main(){
  delay(1000) // Pause for 1 seconds, and then resume
}
```
*Note that a suspend function can run only from within the scope of another Coroutine.*

Even though the code in the main method is executed in a coroutine, it doesn't mean it is non-blocking: the delay 
function call pauzes the program for a second and blocks the main thread.
We will show later how coroutines can be used to run code non-blocking, but for now it suffices to say that by default coroutines are blocking.

#### Continuation
A `suspend` function can be paused and resumed on multiple points, this is handled by `Continuation`. How Continuation is added we can see by looking at the decompiled Java code.
There you will see that an instance of Continuation is passes to suspend transparently.

```Java
public static final Object main(@NotNull Continuation $completion) {
        ...
}
```

The Continuation interface consist out of
- a context, which holds the coroutine-local information and takes important part in `Continuation interception`.
- the function `resumeWith()` which is called with a result of the last suspension point and resumes the coroutine execution.

```Kotlin
interface Continuation<in T> {
    val context: CoroutineContext
    fun resumeWith(result: Result<T>)
}
```

#### Continuation Passing Style 

As mentioned earlier, the suspended function get a `Continuation` as a parameter, and, next to that, the return type will be changed to `Any?`.
`Any?` because a suspended function can either return directly as a normal function, or, return `COROUTINE_SUSPENDED` when it's suspended.
The transformation of suspended function is called continues passing style (CPS). 
The CPS is the magic behind coroutines, it will turn your suspendable function with multiple suspension (points where suspended functions are called) points behind the scenes into a state machine.

#### Continuation in action
Let's take a look at a simple example where we see the continuation in action.

```Kotlin
val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

suspend fun main() {
  val message =  suspendCoroutine<String> { continuation -> //1
    executor.schedule( //2 
      {
        continuation.resumeWith(Result.success("Hello")) // 3
      },
      1000, TimeUnit.MILLISECONDS
    )
  }
  println(message) //4
  println("Continuation")
}
```

1) First we obtained the current continuation with the function `suspendCoroutine`. 
2) Within suspendCoroutine a thread is scheduled to call `resumeWith` after 1000 milliseconds. Meanwhile, the continuation state is `COROUTINE_SUSPENDED`. 
3) After the delay, `resumeWith` is called and coroutine continues with the result `Hello`.
4) Then the message `Hello` and `Continuation` gets printed.


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
