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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class RecipeController {
    private final RecipesService recipeService;

    @PostMapping("recipes")
    public ResponseEntity<RecipeResponse> createRecipes(@Valid @RequestBody RecipeRequest recipeRequest) {
        System.out.println(recipeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.saveRecipe(recipeRequest));
    }

    @GetMapping(value = "recipes")
    public ResponseEntity<List<RecipeResponse>> getRecipes() {
        return ResponseEntity.ok(recipeService.getRecipes());
    }

    @GetMapping(value = "recipes/{id}")
    public ResponseEntity<RecipeResponse> getRecipe(@PathVariable() Long id) {
        return ResponseEntity.ok(recipeService.getRecipe(id));
    }

    @PutMapping(value = "recipes/{id}")
    public ResponseEntity<RecipeResponse> updateRecipe(@PathVariable() Long id, @Valid @RequestBody RecipeRequest recipeRequest) {
        return ResponseEntity.ok(recipeService.updateOrCreateRecipe(id, recipeRequest));
    }

    @DeleteMapping("recipes/{id}")
    public ResponseEntity<Void> deleteRecipes(@PathVariable() Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
