package nl.rabobank.kotlinmovement.recipes.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IngredientResponse {
    private final Long id;
    @NotNull
    private final String name;
    @NotNull
    private final IngredientType type;
    @NotNull
    private final int weight;
}
