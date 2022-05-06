package nl.rabobank.kotlinmovement.recipes.service;

import nl.rabobank.kotlinmovement.recipes.data.IngredientsEntityJ;
import nl.rabobank.kotlinmovement.recipes.data.RecipesEntityJ;
import nl.rabobank.kotlinmovement.recipes.domain.IngredientResponseJ;
import nl.rabobank.kotlinmovement.recipes.domain.IngredientTypeJ;
import nl.rabobank.kotlinmovement.recipes.domain.RecipeRequestJ;
import nl.rabobank.kotlinmovement.recipes.domain.RecipeResponseJ;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;

final class RecipesMapperJ {
    static RecipesEntityJ toRecipeEntity(RecipeRequestJ recipeRequestJ) {
        return new RecipesEntityJ(null, recipeRequestJ.getRecipeName(), emptySet());
    }

    static Set<IngredientsEntityJ> toIngredientsEntity(RecipeRequestJ recipeRequestJ, RecipesEntityJ recipe) {
        return recipeRequestJ.getIngredients()
                .stream()
                .map(it -> new IngredientsEntityJ(recipe, null, it.getName(), it.getType().name(), it.getWeight()))
                .collect(Collectors.toSet());
    }

    static RecipeResponseJ toRecipeResponse(RecipesEntityJ recipes, Set<IngredientsEntityJ> ingredientsEntities) {
        return new RecipeResponseJ(recipes.getId(), recipes.getRecipeName(),
                ingredientsEntities.stream()
                        .map(it -> new IngredientResponseJ(it.getId(), it.getName(), IngredientTypeJ.valueOf(it.getType()), it.getWeight()))
                        .collect(Collectors.toSet()));
    }

}
