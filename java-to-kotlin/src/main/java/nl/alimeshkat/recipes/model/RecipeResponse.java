package nl.alimeshkat.recipes.model;


import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Data
public final class RecipeResponse {
    @NotNull
    private final Long id;
    @NotNull
    private final String recipeName;
    @NotNull
    private final Set<IngredientResponse> ingredients;
}
