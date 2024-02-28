# Project Setup

In order to make the `Recipe` service fully non-blocking we will have to change our project setup and change the code in the repository .

What changes?

1. Spring WebMVC (blocking) -> Spring Reactor (non-blocking)
2. JDBC connection (blocking) -> R2DBC (non-blocking)


