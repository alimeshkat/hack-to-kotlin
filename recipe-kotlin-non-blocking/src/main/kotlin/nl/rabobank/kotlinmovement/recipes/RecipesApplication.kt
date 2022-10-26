package nl.rabobank.kotlinmovement.recipes

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableTransactionManagement
@EnableR2dbcRepositories
@EnableR2dbcAuditing
@SpringBootApplication
class RecipesApplication

fun main(args: Array<String>) {
    runApplication<RecipesApplication>(*args)
}
