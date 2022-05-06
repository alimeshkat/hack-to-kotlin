package nl.rabobank.kotlinmovement.recipes.controller;

import lombok.AllArgsConstructor;
import nl.rabobank.kotlinmovement.recipes.domain.RecipeRequestJ;
import nl.rabobank.kotlinmovement.recipes.domain.RecipeResponseJ;
import nl.rabobank.kotlinmovement.recipes.service.RecipesServiceJ;
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
public class RecipeControllerJ {
    private final RecipesServiceJ recipeService;

    @PostMapping("recipes")
    public ResponseEntity<RecipeResponseJ> createRecipes(@Valid @RequestBody RecipeRequestJ recipeRequestJ) {
        System.out.println(recipeRequestJ);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.saveRecipe(recipeRequestJ));
    }

    @GetMapping(value = "recipes")
    public ResponseEntity<List<RecipeResponseJ>> getRecipes(Pageable pageable) {
        return ResponseEntity.ok(recipeService.getRecipes(pageable));
    }

    @GetMapping(value = "recipes/{id}")
    public ResponseEntity<RecipeResponseJ> getRecipe(@PathVariable() Long id) {
        return ResponseEntity.ok(recipeService.getRecipe(id));
    }

    @PutMapping(value = "recipes/{id}")
    public ResponseEntity<RecipeResponseJ> updateRecipe(@PathVariable() Long id, @Valid @RequestBody RecipeRequestJ recipeRequestJ) {
        return ResponseEntity.ok(recipeService.updateOrCreateRecipe(id, recipeRequestJ));
    }

    @DeleteMapping("recipes/{id}")
    public ResponseEntity<Void> deleteRecipes(@PathVariable() Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
