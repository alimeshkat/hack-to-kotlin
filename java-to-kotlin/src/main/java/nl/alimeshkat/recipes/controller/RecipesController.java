package nl.alimeshkat.recipes.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.alimeshkat.recipes.model.RecipeRequest;
import nl.alimeshkat.recipes.model.RecipeResponse;
import nl.alimeshkat.recipes.service.RecipesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Iterable<RecipeResponse>> getRecipes() {
        log.info("Get All Recipes");
        return ResponseEntity.ok(recipeService.getRecipes());
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
