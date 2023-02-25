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
- It supports Kotlin Coroutines! More about this in the next section [An Introduction to Coroutines ](IntroductionCoroutine.md)