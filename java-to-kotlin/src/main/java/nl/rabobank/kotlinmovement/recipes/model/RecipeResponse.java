package nl.rabobank.kotlinmovement.recipes.model;


import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Data
public class RecipeResponse {
    @NotNull
    private final Long id;
    @NotNull
    private final String recipeName;
    @NotNull
    private final Set<IngredientResponse> ingredients;
}
