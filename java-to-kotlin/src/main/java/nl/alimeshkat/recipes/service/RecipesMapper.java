package nl.alimeshkat.recipes.service;

import nl.alimeshkat.recipes.data.IngredientsEntity;
import nl.alimeshkat.recipes.data.RecipesEntity;
import nl.alimeshkat.recipes.model.IngredientResponse;
import nl.alimeshkat.recipes.model.IngredientType;
import nl.alimeshkat.recipes.model.RecipeRequest;
import nl.alimeshkat.recipes.model.RecipeResponse;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;

public final class RecipesMapper {
    public static RecipesEntity toRecipeEntity(RecipeRequest recipeRequest) {
        return new RecipesEntity(null, recipeRequest.getRecipeName(), emptySet());
    }

    public static Set<IngredientsEntity> toIngredientsEntity(RecipeRequest recipeRequest, RecipesEntity recipe) {
        return recipeRequest.getIngredients()
                .stream()
                .map(it -> new IngredientsEntity(recipe, null, it.getName(), it.getType().name(), it.getWeight()))
                .collect(Collectors.toSet());
    }

    public static RecipeResponse toRecipeResponse(RecipesEntity recipes, Set<IngredientsEntity> ingredientsEntities) {
        return new RecipeResponse(recipes.getRecipeId(), recipes.getRecipeName(),
                ingredientsEntities.stream()
                        .map(it -> new IngredientResponse(it.getIngredientId(), it.getName(), IngredientType.valueOf(it.getType()), it.getWeight()))
                        .collect(Collectors.toSet()));
    }

}
