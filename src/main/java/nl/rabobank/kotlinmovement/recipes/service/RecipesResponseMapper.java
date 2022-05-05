package nl.rabobank.kotlinmovement.recipes.service;

import nl.rabobank.kotlinmovement.recipes.data.entity.IngredientsEntity;
import nl.rabobank.kotlinmovement.recipes.data.entity.RecipesEntity;
import nl.rabobank.kotlinmovement.recipes.domain.IngredientResponse;
import nl.rabobank.kotlinmovement.recipes.domain.RecipeResponse;

import java.util.Set;
import java.util.stream.Collectors;

final class RecipesResponseMapper {

    static RecipeResponse toRecipeResponse(RecipesEntity recipes, Set<IngredientsEntity> ingredientsEntities) {
        return new RecipeResponse(recipes.getId(), recipes.getRecipeName(),
                ingredientsEntities.stream()
                        .map(it -> new IngredientResponse(it.getId(), it.getName(), it.getType(), it.getWeight()))
                        .collect(Collectors.toSet()));
    }

}
