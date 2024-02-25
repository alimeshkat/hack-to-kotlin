package nl.rabobank.kotlinmovement.recipes.model;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public final class RecipeRequest {
    @NotBlank( message = "recipeName")
    final String recipeName;
    @NotEmpty(message = "ingredients")
    @Valid
    private final Set<IngredientRequest> ingredients;
}
