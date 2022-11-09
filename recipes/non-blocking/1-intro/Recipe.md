# Intro

It turned out, the `Recipe` service has become populair. But unfortunately under the new
tremendous load (10,000s req/sec), it's not performing that great and sometimes becomes unresponsive. We are now
thinking about doing things differently by
refactoring the code once again.

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

So in short why we want to use Spring WebFlux:

- It implements the Reactive Streams which offers a standard for asynchronous stream processing with non-blocking back
  pressure.
- It runs on servers such as Netty, Undertow, and Servlet 3.1+ containers.
- It supports Kotlin Coroutines!

### Coroutines

*"A coroutine is an instance of suspendable computation.
It is conceptually similar to a thread,
in the sense that it takes a block of code to run that works concurrently with the rest of the code.
However, a coroutine is not bound to any particular thread.
It may suspend its execution in one thread and resume in another one."*

Kotlin coroutine can be viewed as a light-weight thread (or a callback), you can create thousands
of them without running out of memory.

**Suspend & Continue**

Kotlin provides on language level the basis for coroutine with the `suspend` modifier for functions
and `kotlin.coroutines.Continuation` interface.

```Kotlin
interface Continuation<in T> {
    val context: CoroutineContext
    fun resumeWith(result: Result<T>)
}
```

A suspend function can be paused and resumed on multiple points.
An instance of `Continuation` is passes to suspend transparently.
Let's take a look at a simple suspending main function:

```Kotlin
suspend fun main() {
    //
}
```

Decompiled the above code to Java, and you see the Continuation parameter:

```Java
public static final main(@NotNull Continuation completion){}
```

So lets use the Continuation to do stuff

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

## Builders

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
