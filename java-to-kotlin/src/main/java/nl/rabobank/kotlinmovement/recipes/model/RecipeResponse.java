package nl.rabobank.kotlinmovement.recipes.model;


import lombok.Data;

import java.util.Set;

@Data
public class RecipeResponse {
    private final Long id;
    private final String recipeName;
    private final Set<IngredientResponse> ingredients;
}
