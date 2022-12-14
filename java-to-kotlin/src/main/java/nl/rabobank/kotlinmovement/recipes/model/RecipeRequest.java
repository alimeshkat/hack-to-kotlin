package nl.rabobank.kotlinmovement.recipes.model;


import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public final class RecipeRequest {
    @NotBlank( message = "recipeName")
    final String recipeName;
    @NotEmpty(message = "ingredients")
    @Valid
    private final Set<IngredientRequest> ingredients;
}
