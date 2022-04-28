package nl.rabobank.kotlinmovement.recipes.controller;

import lombok.AllArgsConstructor;
import nl.rabobank.kotlinmovement.recipes.domain.model.RecipeRequest;
import nl.rabobank.kotlinmovement.recipes.domain.model.RecipeResponse;
import nl.rabobank.kotlinmovement.recipes.service.RecipesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PizzaRecipeController {
    private final RecipesService recipeService;

    @PostMapping("recipes")
    public ResponseEntity<RecipeResponse> createPizzaRecipes(@RequestBody RecipeRequest recipeRequest) {
        System.out.println(recipeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.saveRecipe(recipeRequest));
    }

    @GetMapping(value = "recipes/{id}")
    public ResponseEntity<RecipeResponse> getPizzaRecipes(@PathVariable() Long id) {
        return ResponseEntity.ok(recipeService.geRecipe(id));
    }

    @DeleteMapping("recipes/{id}")
    public ResponseEntity<Void> deletePizzaRecipes(@PathVariable() Long id) {
        recipeService.deletePizzaRecipe(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
