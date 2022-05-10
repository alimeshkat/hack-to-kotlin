package nl.rabobank.kotlinmovement.recipes.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class IngredientRequest {
    @NotBlank
    private final String name;
    @NotBlank
    private final IngredientType type;
    @NotBlank
    private final int weight;
}

