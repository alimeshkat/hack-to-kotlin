package nl.rabobank.kotlinmovement.recipes;

import nl.rabobank.kotlinmovement.recipes.model.IngredientType;

public class IngredientResponseTestDTO {
    private final Long id;
    private final String name;
    private final IngredientType type;
    private final int weight;

    public IngredientResponseTestDTO(){
        this(null, null, null, 0);
    }
    public IngredientResponseTestDTO(Long id, String name, IngredientType type, int weight) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public IngredientType getType() {
        return type;
    }

    public int getWeight() {
        return weight;
    }
}
