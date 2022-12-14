package nl.rabobank.kotlinmovement.recipes.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public final class IngredientRequest {
    @NotBlank(message = "ingredient.name")
    private final String name;
    @NotNull(message = "ingredient.type")
    private final IngredientType type;
    @NotNull(message = "ingredient.weight")
    private final Integer weight;
}

