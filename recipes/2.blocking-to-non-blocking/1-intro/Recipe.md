# Intro

It turned out, the `Recipe` service has become popular. But unfortunately under the new
tremendous load (1000 tps), it's not performing that great and sometimes becomes unresponsive. We are now
thinking about doing things differently by refactoring the code once again.

## Blocking

Why our service is not performing that great? Well one reason is because it's blocking.
The thread that handles the call to the Recipe service gets blocked when waiting on the result from the recipes'
database, i.e. blocking `I/O` operation.
Because threads are expensive in terms of CPU and memory usage you cannot create unlimited amount of them, especially in
containerized environments where
resources are restricted. So we have a pool of them and reuse them whenever we can. But, In peak moments, when all
available threads are running, or are waiting on another thread performing something can exhaust the servers thread pool
that renders the app to become unresponsiveness.

## Non-Blocking

So, what can we do to circumvent this issue? Can we make the calls to the Recipe service `non-blocking`?
Non-blocking means that the thread that handles the call to the Recipe service is not blocked when waiting on the result
from the recipes' database. Instead, it's free to do other things, when the result is ready, the thread will be notified
and can continue processing the result ike handling other requests.
When the result is ready, the thread will be notified and can continue processing the result.


### Spring WebFlux
To make our Recipe service non-blocking, we can use the counterpart to `Spring Web`, the `Spring WebFlux`

In short why we want to use Spring WebFlux:

- It implements the `Reactive Streams` specification which offers a standard for asynchronous stream processing with
  non-blocking back pressure
- It runs on industry standard servers such as Netty, Undertow, and Servlet 3.1+ containers
- It supports Kotlin Coroutines! 

More about Spring Boot reactor and Coroutines in the next sections where we will explore how to make the Recipe service non-blocking!

[NEXT](../2-project-setup/Recipe.md)
