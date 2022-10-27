package nl.rabobank.kotlinmovement.recipes.service;

import lombok.AllArgsConstructor;
import nl.rabobank.kotlinmovement.recipes.data.IngredientsEntity;
import nl.rabobank.kotlinmovement.recipes.data.IngredientsRepository;
import nl.rabobank.kotlinmovement.recipes.data.RecipesEntity;
import nl.rabobank.kotlinmovement.recipes.data.RecipesRepository;
import nl.rabobank.kotlinmovement.recipes.model.RecipeRequest;
import nl.rabobank.kotlinmovement.recipes.model.RecipeResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static nl.rabobank.kotlinmovement.recipes.service.RecipesMapper.toIngredientsEntity;
import static nl.rabobank.kotlinmovement.recipes.service.RecipesMapper.toRecipeEntity;
import static nl.rabobank.kotlinmovement.recipes.service.RecipesMapper.toRecipeResponse;

@Service
@AllArgsConstructor
public class RecipesService {
    private final RecipesRepository recipeRepository;
    private final IngredientsRepository ingredientsRepository;

    @Transactional
    public RecipeResponse getRecipe(long id) {
        var recipe = recipeRepository.findById(id);
        return recipe.map(r -> toRecipeResponse(r, r.getIngredients()))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Recipe %d not found", id)));
    }

    @Transactional
    public List<RecipeResponse> getRecipes() {
        return recipeRepository.findAll()
                .stream()
                .map(r -> toRecipeResponse(r, r.getIngredients()))
                .collect(Collectors.toList());
    }

    @Transactional
    public RecipeResponse saveRecipe(RecipeRequest recipeRequest) {
        final RecipesEntity recipe = toRecipeEntity(recipeRequest);
        var recipes = recipeRepository.save(recipe);
        var ingredients = saveIngredients(recipeRequest, recipes);
        return toRecipeResponse(recipes, ingredients);
    }

    @Transactional
    public RecipeResponse updateOrCreateRecipe(Long id, RecipeRequest recipeRequest) {
        return recipeRepository.findById(id)
                .map(it -> {
                    var recipe = recipeRepository.save(new RecipesEntity(it.getId(), recipeRequest.getRecipeName(), Collections.emptySet()));
                    var ingredients = saveIngredients(recipeRequest, recipe);
                    return toRecipeResponse(recipe, ingredients);
                }).
                orElse(saveRecipe(recipeRequest));
    }

    @Transactional
    public void deleteRecipe(long id) {
        try {
            recipeRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format("Recipe %d not found", id));
        }
    }

    private Set<IngredientsEntity> saveIngredients(RecipeRequest recipeRequest, RecipesEntity recipe) {
        final Set<IngredientsEntity> ingredients = toIngredientsEntity(recipeRequest, recipe);
        return new HashSet<>(ingredientsRepository.saveAll(ingredients));
    }

}
