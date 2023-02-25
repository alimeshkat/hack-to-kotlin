# Coroutine API

In the previous section we explained the basics of Coroutines on the language level, but now we will cover the most essential parts of high
level Coroutines library [`kotlinx-coroutines`](https://github.com/Kotlin/kotlinx.coroutines).

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
