package nl.rabobank.kotlinmovement.recipes;

import nl.rabobank.kotlinmovement.recipes.model.IngredientResponse;

import java.util.Set;

public class RecipeResponseTestDTO {
    private final Long id;
    private final String recipeName;
    private final Set<IngredientResponseTestDTO> ingredients;

    public RecipeResponseTestDTO(){
        this(null, null,null);
    }
    public RecipeResponseTestDTO(Long id, String recipeName, Set<IngredientResponseTestDTO> ingredients) {
        this.id = id;
        this.recipeName = recipeName;
        this.ingredients = ingredients;
    }

    public Long getId() {
        return id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public Set<IngredientResponseTestDTO> getIngredients() {
        return ingredients;
    }
}
