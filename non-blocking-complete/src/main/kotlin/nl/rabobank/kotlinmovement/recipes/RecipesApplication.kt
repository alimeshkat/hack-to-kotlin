package nl.rabobank.kotlinmovement.recipes

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class RecipesApplication

fun main(args: Array<String>) {
    runApplication<RecipesApplication>(*args)
}
