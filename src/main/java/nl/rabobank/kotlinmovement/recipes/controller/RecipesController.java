package nl.rabobank.kotlinmovement.recipes.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.kotlinmovement.recipes.model.RecipeRequest;
import nl.rabobank.kotlinmovement.recipes.model.RecipeResponse;
import nl.rabobank.kotlinmovement.recipes.service.RecipesService;
import org.springframework.data.domain.Pageable;
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
@Slf4j
public class RecipesController {
    private final RecipesService recipeService;

    @PostMapping("recipes")
    public ResponseEntity<RecipeResponse> createRecipes(@Valid @RequestBody RecipeRequest recipeRequest) {
        log.info("Create Recipes {}", recipeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.saveRecipe(recipeRequest));

    }

    @GetMapping(value = "recipes")
    public ResponseEntity<List<RecipeResponse>> getRecipes(Pageable pageable) {
        log.info("Get All Recipes");
        return ResponseEntity.ok(recipeService.getRecipes(pageable));
    }

    @GetMapping(value = "recipes/{id}")
    public ResponseEntity<RecipeResponse> getRecipe(@PathVariable() Long id) {
        log.info("Get Recipe with id: {}", id);
        return ResponseEntity.ok(recipeService.getRecipe(id));
    }

    @PutMapping(value = "recipes/{id}")
    public ResponseEntity<RecipeResponse> updateRecipe(@PathVariable() Long id, @Valid @RequestBody RecipeRequest recipeRequest) {
        log.info("Update Recipe with id: {}. Update: {}", id, recipeRequest);
        return ResponseEntity.ok(recipeService.updateOrCreateRecipe(id, recipeRequest));
    }

    @DeleteMapping("recipes/{id}")
    public ResponseEntity<Void> deleteRecipes(@PathVariable() Long id) {
        log.info("Delete Recipe {}", id);
        recipeService.deleteRecipe(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
