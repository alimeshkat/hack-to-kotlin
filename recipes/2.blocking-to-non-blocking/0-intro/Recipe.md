# Intro: blocking to non-blocking

In the`java-to-kotlin` module, we learned how to convert a `Java` `Spring Boot` application to a `Kotlin` `Spring Boot`
application. Now, we are going to explore one of the most important features of Kotlin: `Coroutines`, and learn how to
use them in a `Spring Boot` application.

# Objectives

- Learn about the difference between `blocking` and `non-blocking` code
- Learn about the `Reactive Streams` specification and how `Coroutines` can be used to implement it in a Spring Boot
  application

# Index
- [0-Intro](Recipe.md)
- [1-project-setup](../1-project-setup/Recipe.md)
- [2-data](../2-data/Recipe.md)
- [3-service](../3-service/Recipe.md)
- [4-controller](../4-controller/Recipe.md)

## Blocking vs Non-Blocking

Imagine that the `Recipe` service has become very popular among the community of bakers. This popularity comes with a
cost: under the new tremendous load (1000 tps), its performance has decreased and it sometimes becomes unresponsive. We
are now considering refactoring the code once again to improve performance.

### Blocking

Why isn't our service performing well? One reason is that it's blocking. The thread that handles the call to the Recipe
service gets blocked when waiting on the result from the recipes' database, i.e., blocking `I/O` operation. Because
threads are resource-intensive in terms of CPU and memory usage, you cannot create an unlimited number of them,
especially in containerized environments where resources are limited. So, we maintain a pool of threads and reuse them
whenever possible. However, during peak times, when all available threads are either running or waiting for another
thread to complete a task, it can exhaust the server's thread pool, causing the application to become unresponsive.

### Non-Blocking

So, what can we do to address this issue? Can we make the calls to the Recipe service `non-blocking`? Non-blocking means
that the thread that handles the call to the Recipe service is not blocked when waiting on the result from the recipes'
database. Instead, it's free to do other things, and when the result is ready, the thread will be notified and can
continue processing the result.

### Spring WebFlux

To make our Recipe service non-blocking, we can use the counterpart to `Spring Web`, the `Spring WebFlux`

In short, why we want to use Spring WebFlux:

- It implements the `Reactive Streams` specification which offers a standard for asynchronous stream processing with
  non-blocking back pressure
- It runs on industry-standard servers such as Netty, Undertow, and Servlet 3.1+ containers
- It supports Kotlin Coroutines!

More about Spring Boot reactor and Coroutines in the next sections where we will explore how to make the Recipe service
non-blocking!

[NEXT](../1-project-setup/Recipe.md)
