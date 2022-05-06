package nl.rabobank.kotlinmovement.recipes.service;

import lombok.AllArgsConstructor;
import nl.rabobank.kotlinmovement.recipes.data.IngredientsEntityJ;
import nl.rabobank.kotlinmovement.recipes.data.IngredientsRepositoryJ;
import nl.rabobank.kotlinmovement.recipes.data.RecipesEntityJ;
import nl.rabobank.kotlinmovement.recipes.data.RecipesRepositoryJ;
import nl.rabobank.kotlinmovement.recipes.domain.RecipeRequestJ;
import nl.rabobank.kotlinmovement.recipes.domain.RecipeResponseJ;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static nl.rabobank.kotlinmovement.recipes.service.RecipesMapperJ.toIngredientsEntity;
import static nl.rabobank.kotlinmovement.recipes.service.RecipesMapperJ.toRecipeEntity;
import static nl.rabobank.kotlinmovement.recipes.service.RecipesMapperJ.toRecipeResponse;

@Service
@AllArgsConstructor
public class RecipesServiceJ {
    private final RecipesRepositoryJ recipeRepository;
    private final IngredientsRepositoryJ ingredientsRepositoryJ;

    @Transactional
    public RecipeResponseJ getRecipe(Long id) {
        var recipe = recipeRepository.findById(id);
        return recipe.map(r -> toRecipeResponse(r, r.getIngredients()))
                .orElseThrow(ResourceNotFoundExceptionJ::new);
    }

    @Transactional
    public List<RecipeResponseJ> getRecipes(Pageable pageable) {
        final Spliterator<RecipesEntityJ> recipes = recipeRepository.findAll(pageable).spliterator();
        return StreamSupport.stream(recipes, false)
                .map(r -> toRecipeResponse(r, r.getIngredients()))
                .collect(Collectors.toList());
    }
    @Transactional
    public RecipeResponseJ saveRecipe(RecipeRequestJ recipeRequestJ) {
        final RecipesEntityJ recipe = toRecipeEntity(recipeRequestJ);
        var recipes = recipeRepository.save(recipe);
        var ingredients = saveIngredients(recipeRequestJ, recipes);
        return toRecipeResponse(recipes, ingredients);
    }

    @Transactional
    public RecipeResponseJ updateOrCreateRecipe(Long id, RecipeRequestJ recipeRequestJ) {
        RecipesEntityJ recipes = updateOrCreateRecipes(id, recipeRequestJ);
        final Set<IngredientsEntityJ> ingredients = saveIngredients(recipeRequestJ, recipes);
        return toRecipeResponse(recipes, ingredients);
    }

    @Transactional
    public void deleteRecipe(Long id) {
        try {
            recipeRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundExceptionJ();
        }
    }

    private RecipesEntityJ updateOrCreateRecipes(Long id, RecipeRequestJ recipeRequestJ) {
        return recipeRepository.findById(id).map(it -> {
            it.setRecipeName(recipeRequestJ.getRecipeName());
            it.getIngredients().clear();
            return it;
        }).orElse(recipeRepository.save(toRecipeEntity(recipeRequestJ)));
    }

    private Set<IngredientsEntityJ> saveIngredients(RecipeRequestJ recipeRequestJ, RecipesEntityJ recipe) {
        final Set<IngredientsEntityJ> ingredients = toIngredientsEntity(recipeRequestJ, recipe);
        return StreamSupport.stream(
                ingredientsRepositoryJ
                        .saveAll(ingredients)
                        .spliterator(), false
        ).collect(Collectors.toSet());
    }

}
