package nl.rabobank.kotlinmovement.recipes.service;

import lombok.AllArgsConstructor;
import nl.rabobank.kotlinmovement.recipes.domain.model.IngredientResponse;
import nl.rabobank.kotlinmovement.recipes.domain.model.RecipeRequest;
import nl.rabobank.kotlinmovement.recipes.domain.model.RecipeResponse;
import nl.rabobank.kotlinmovement.recipes.domain.entity.IngredientsEntity;
import nl.rabobank.kotlinmovement.recipes.domain.entity.RecipesEntity;
import nl.rabobank.kotlinmovement.recipes.exeption.ResourceNotFoundException;
import nl.rabobank.kotlinmovement.recipes.service.repository.IngredientsRepository;
import nl.rabobank.kotlinmovement.recipes.service.repository.RecipesRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class RecipesService {
    private final RecipesRepository pizzaRecipeRepository;
    private final IngredientsRepository ingredientsRepository;

    public RecipeResponse saveRecipe(RecipeRequest recipeRequest) {
        final RecipesEntity recipe = recipesEntity(recipeRequest);
        var recipes = pizzaRecipeRepository.save(recipe);

        final Set<IngredientsEntity> ingredients = mapToIngredientsEntity(recipeRequest, recipe);

        final Set<IngredientsEntity> ingredientsEntities =
                StreamSupport.stream(
                        ingredientsRepository.saveAll(ingredients).spliterator(), false
                ).collect(Collectors.toSet());

        return mapToResponse(recipes, ingredientsEntities);
    }

    public RecipeResponse geRecipe(Long id) {
        var recipe = pizzaRecipeRepository.findById(id);
        return recipe.map(r -> mapToResponse(r, r.getIngredients()))
                .orElseThrow(ResourceNotFoundException::new);
    }

    private RecipesEntity recipesEntity(RecipeRequest recipeRequest) {
        return new RecipesEntity(null, recipeRequest.getRecipeName(), null);
    }

    public void deletePizzaRecipe(Long id) {
        pizzaRecipeRepository.deleteById(id);
    }

    private Set<IngredientsEntity> mapToIngredientsEntity(RecipeRequest recipeRequest, RecipesEntity recipe) {
        return recipeRequest.getIngredients()
                .stream()
                .map(it -> new IngredientsEntity(recipe, null, it.getName(), it.getType(), it.getWeight()))
                .collect(Collectors.toSet());
    }

    private RecipeResponse mapToResponse(RecipesEntity recipes, Set<IngredientsEntity> ingredientsEntities) {
        return new RecipeResponse(recipes.getId(), recipes.getRecipeName(),
                ingredientsEntities.stream()
                        .map(it -> new IngredientResponse(it.getId(), it.getName(), it.getType(), it.getWeight()))
                        .collect(Collectors.toSet()));
    }
}
