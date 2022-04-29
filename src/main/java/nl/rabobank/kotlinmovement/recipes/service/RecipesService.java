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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Collections.emptySet;

@Service
@AllArgsConstructor
public class RecipesService {
    private final RecipesRepository recipeRepository;
    private final IngredientsRepository ingredientsRepository;

    public RecipeResponse saveRecipe(RecipeRequest recipeRequest) {
        final RecipesEntity recipe = toRecipeEntity(recipeRequest);
        var recipes = recipeRepository.save(recipe);
        var ingredients = saveIngredients(recipeRequest, recipes);
        return toRecipeResponse(recipes, ingredients);
    }

    public RecipeResponse getRecipe(Long id) {
        var recipe = recipeRepository.findById(id);
        return recipe.map(r -> toRecipeResponse(r, r.getIngredients()))
                .orElseThrow(ResourceNotFoundException::new);
    }

    public List<RecipeResponse> getRecipes() {
        return StreamSupport.stream(recipeRepository.findAll().spliterator(), false)
                .map(r -> toRecipeResponse(r, r.getIngredients())).collect(Collectors.toList());
    }

    public RecipeResponse updateOrCreateRecipe(Long id, RecipeRequest recipeRequest) {
        var recipes = recipeRepository.findById(id).map(it -> {
            it.setRecipeName(recipeRequest.getRecipeName());
            it.getIngredients().clear();
            return it;
        }).orElse(recipeRepository.save(toRecipeEntity(recipeRequest)));

        final Set<IngredientsEntity> ingredients = saveIngredients(recipeRequest, recipes);
        return toRecipeResponse(recipes, ingredients);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    private Set<IngredientsEntity> saveIngredients(RecipeRequest recipeRequest, RecipesEntity recipe) {
        final Set<IngredientsEntity> ingredients = toIngredientsEntity(recipeRequest, recipe);
        return StreamSupport.stream(
                ingredientsRepository.saveAll(ingredients).spliterator(), false
        ).collect(Collectors.toSet());
    }

    private RecipesEntity toRecipeEntity(RecipeRequest recipeRequest) {
        return new RecipesEntity(null, recipeRequest.getRecipeName(), emptySet());
    }

    private Set<IngredientsEntity> toIngredientsEntity(RecipeRequest recipeRequest, RecipesEntity recipe) {
        return recipeRequest.getIngredients()
                .stream()
                .map(it -> new IngredientsEntity(recipe, null, it.getName(), it.getType(), it.getWeight()))
                .collect(Collectors.toSet());
    }

    private RecipeResponse toRecipeResponse(RecipesEntity recipes, Set<IngredientsEntity> ingredientsEntities) {
        return new RecipeResponse(recipes.getId(), recipes.getRecipeName(),
                ingredientsEntities.stream()
                        .map(it -> new IngredientResponse(it.getId(), it.getName(), it.getType(), it.getWeight()))
                        .collect(Collectors.toSet()));
    }
}
