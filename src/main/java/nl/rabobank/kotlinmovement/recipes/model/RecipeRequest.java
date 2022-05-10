package nl.rabobank.kotlinmovement.recipes.model;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class RecipeRequest {
    @NotBlank
    private final String recipeName;
    @NotEmpty
    @NotNull
    private final Set<IngredientRequest> ingredients;
}
