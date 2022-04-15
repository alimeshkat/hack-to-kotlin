package nl.rabobank.kotlinmovement.pizzarecipes;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PizzaRecipeController {
    private final PizzaRecipeService recipeService;

    @PostMapping("pizza-recipes")
    public ResponseEntity<PizzaRecipeResponse> getPizzaRecipes(PizzaRecipeRequest pizzaRecipeRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.savePizzaRecipe(pizzaRecipeRequest));
    }

    @GetMapping(value = "pizza-recipes/{id}")
    public ResponseEntity<PizzaRecipeResponse> getPizzaRecipes(@PathVariable() Long id) {
        return ResponseEntity.ok(recipeService.getPizzaRecipe(id));
    }

    @DeleteMapping("pizza-recipes/{id}")
    public ResponseEntity<Void> deletePizzaRecipes(@PathVariable() Long id) {
        recipeService.deletePizzaRecipe(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
