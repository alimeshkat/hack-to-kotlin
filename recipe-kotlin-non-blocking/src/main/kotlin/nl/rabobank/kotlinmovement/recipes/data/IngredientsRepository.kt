package nl.rabobank.kotlinmovement.recipes.data

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface IngredientsRepository : CoroutineCrudRepository<IngredientsEntity, Long>
