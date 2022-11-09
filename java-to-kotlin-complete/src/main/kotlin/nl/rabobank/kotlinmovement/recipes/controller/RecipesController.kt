package nl.rabobank.kotlinmovement.recipes.controller

import nl.rabobank.kotlinmovement.recipes.model.RecipeRequest
import nl.rabobank.kotlinmovement.recipes.model.RecipeResponse
import nl.rabobank.kotlinmovement.recipes.service.RecipesService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class RecipesController(private val recipeService: RecipesService) {

    @PostMapping("recipes")
    fun createRecipes(@Valid @RequestBody recipeRequest: RecipeRequest): ResponseEntity<RecipeResponse> {
        log.info("Create Recipes $recipeRequest")
        return ResponseEntity.status(HttpStatus.CREATED).body(
            recipeService.saveRecipe(recipeRequest)
        )
    }

    @get:GetMapping(value = ["recipes"])
    val recipes: ResponseEntity<Iterable<RecipeResponse>>
        get() {
            log.info("Get All Recipes")
            return ResponseEntity.ok(recipeService.recipes)
        }

    @GetMapping(value = ["recipes/{id}"])
    fun getRecipe(@PathVariable id: Long): ResponseEntity<RecipeResponse> {
        log.info("Get Recipe with $id")
        return ResponseEntity.ok(recipeService.getRecipe(id))
    }

    @PutMapping(value = ["recipes/{id}"])
    fun updateRecipe(
        @PathVariable id: Long,
        @Valid @RequestBody recipeRequest: RecipeRequest
    ): ResponseEntity<RecipeResponse> {
        log.info("Update Recipe with id: $id. Update: $recipeRequest")
        return ResponseEntity.ok(recipeService.updateOrCreateRecipe(id, recipeRequest))
    }

    @DeleteMapping("recipes/{id}")
    fun deleteRecipes(@PathVariable id: Long): ResponseEntity<Void> {
        log.info("Delete Recipe $id")
        recipeService.deleteRecipe(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    companion object  {
        val log: Logger = LoggerFactory.getLogger(RecipesController::class.java)
    }
}

