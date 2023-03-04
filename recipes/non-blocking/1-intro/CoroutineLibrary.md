# Coroutine API

In the previous section we explained the basics of Coroutines on the language level, in this
section we will focus on the library that utilizes the Kotlin's language features `suspend` and `Continuation` to offer a rich and
safe set of utilities to write coroutines. This library is called [`kotlinx-coroutines`](https://github.com/Kotlin/kotlinx.coroutines) and is not part of the standard library.

We will first learn all about coroutine creation with the `Coroutine builders`

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
