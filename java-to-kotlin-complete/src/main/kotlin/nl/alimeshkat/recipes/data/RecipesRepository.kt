package nl.alimeshkat.recipes.data

import org.springframework.data.jpa.repository.JpaRepository

interface RecipesRepository : JpaRepository<RecipesEntity, Long>
