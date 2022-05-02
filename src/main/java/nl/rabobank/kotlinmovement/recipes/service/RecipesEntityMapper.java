package nl.rabobank.kotlinmovement.recipes.service;

import nl.rabobank.kotlinmovement.recipes.service.entity.IngredientsEntity;
import nl.rabobank.kotlinmovement.recipes.service.entity.RecipesEntity;
import nl.rabobank.kotlinmovement.recipes.domain.model.RecipeRequest;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;

final class RecipesEntityMapper {
    static RecipesEntity toRecipeEntity(RecipeRequest recipeRequest) {
        return new RecipesEntity(null, recipeRequest.getRecipeName(), emptySet());
    }

    static Set<IngredientsEntity> toIngredientsEntity(RecipeRequest recipeRequest, RecipesEntity recipe) {
        return recipeRequest.getIngredients()
                .stream()
                .map(it -> new IngredientsEntity(recipe, null, it.getName(), it.getType(), it.getWeight()))
                .collect(Collectors.toSet());
    }

}
