package nl.rabobank.kotlinmovement.recipes.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public final class IngredientRequest {
    @NotBlank(message = "ingredient.name")
    private final String name;
    @NotNull(message = "ingredient.type")
    private final IngredientType type;
    @NotNull(message = "ingredient.weight")
    private final Integer weight;
}

