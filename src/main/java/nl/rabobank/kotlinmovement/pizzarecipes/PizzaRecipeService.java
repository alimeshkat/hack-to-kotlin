package nl.rabobank.kotlinmovement.pizzarecipes;

import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PizzaRecipeService {
    private final PizzaRecipeRepository pizzaRecipeRepository;

    public PizzaRecipeResponse savePizzaRecipe(PizzaRecipeRequest pizzaRecipeRequest) {
        var recipe = pizzaRecipeRepository.save(new PizzaRecipeEntity(pizzaRecipeRequest.getId(), pizzaRecipeRequest.getRecipeName()));
        return new PizzaRecipeResponse(recipe.getId(), recipe.getRecipeName());
    }

    public PizzaRecipeResponse getPizzaRecipe(Long id) {
        var recipe = pizzaRecipeRepository.findById(id);
        return recipe.map(r -> new PizzaRecipeResponse(r.getId(), r.getRecipeName())).orElseThrow(ResourceNotFoundException::new);
    }

    public void deletePizzaRecipe(Long id) {
        pizzaRecipeRepository.deleteById(id);
    }
}
