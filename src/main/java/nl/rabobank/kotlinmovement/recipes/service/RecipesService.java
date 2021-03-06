package nl.rabobank.kotlinmovement.recipes.service;

import lombok.AllArgsConstructor;
import nl.rabobank.kotlinmovement.recipes.data.IngredientsEntity;
import nl.rabobank.kotlinmovement.recipes.data.IngredientsRepository;
import nl.rabobank.kotlinmovement.recipes.data.RecipesEntity;
import nl.rabobank.kotlinmovement.recipes.data.RecipesRepository;
import nl.rabobank.kotlinmovement.recipes.model.RecipeRequest;
import nl.rabobank.kotlinmovement.recipes.model.RecipeResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public List<RecipeResponse> getRecipes(Pageable pageable) {
        final Spliterator<RecipesEntity> recipes = recipeRepository.findAll(pageable).spliterator();
        return StreamSupport.stream(recipes, false)
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
        RecipesEntity recipes = updateOrCreateRecipes(id, recipeRequest);
        final Set<IngredientsEntity> ingredients = saveIngredients(recipeRequest, recipes);
        return toRecipeResponse(recipes, ingredients);
    }

    @Transactional
    public void deleteRecipe(long id) {
        try {
            recipeRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format("Recipe %d not found", id));
        }
    }

    private RecipesEntity updateOrCreateRecipes(Long id, RecipeRequest recipeRequest) {
        return recipeRepository.findById(id)
                .map(it -> new RecipesEntity(it.getId(), recipeRequest.getRecipeName(), null)).
                orElse(recipeRepository.save(toRecipeEntity(recipeRequest)));
    }

    private Set<IngredientsEntity> saveIngredients(RecipeRequest recipeRequest, RecipesEntity recipe) {
        final Set<IngredientsEntity> ingredients = toIngredientsEntity(recipeRequest, recipe);
        return StreamSupport.stream(
                ingredientsRepository
                        .saveAll(ingredients)
                        .spliterator(), false
        ).collect(Collectors.toSet());
    }

}
