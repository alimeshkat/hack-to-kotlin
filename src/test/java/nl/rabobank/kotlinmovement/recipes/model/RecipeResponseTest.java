package nl.rabobank.kotlinmovement.recipes.model;

import java.util.Set;

public class RecipeResponseTest {
    private final Long id;
    private final String recipeName;
    private final Set<IngredientResponseTest> ingredients;

    public RecipeResponseTest(){
        this(null, null,null);
    }
    public RecipeResponseTest(Long id, String recipeName, Set<IngredientResponseTest> ingredients) {
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

    public Set<IngredientResponseTest> getIngredients() {
        return ingredients;
    }
}
