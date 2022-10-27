package nl.rabobank.kotlinmovement.recipes.model;

import lombok.Data;

@Data
public class IngredientResponse {
    private final Long id;
    private final String name;
    private final IngredientType type;
    private final int weight;
}
