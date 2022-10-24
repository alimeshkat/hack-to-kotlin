package nl.rabobank.kotlinmovement.recipes.data

import org.springframework.data.jpa.repository.JpaRepository

interface IngredientsRepository : JpaRepository<IngredientsEntity, Long>
