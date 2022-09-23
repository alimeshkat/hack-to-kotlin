package nl.rabobank.kotlinmovement.recipes.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IngredientResponse {
    private final Long id;
    private final String name;
    private final IngredientType type;
    private final int weight;
}
