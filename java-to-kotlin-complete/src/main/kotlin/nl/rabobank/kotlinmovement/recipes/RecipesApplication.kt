package nl.rabobank.kotlinmovement.recipes

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
class RecipesApplication

fun main(args: Array<String>) {
    runApplication<RecipesApplication>(*args)
}
