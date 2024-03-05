package nl.rabobank.kotlinmovement.recipes.test.util.model;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeResponseTest that = (RecipeResponseTest) o;
        return Objects.equals(id, that.id) && Objects.equals(recipeName, that.recipeName) && Objects.equals(ingredients, that.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipeName, ingredients);
    }

    @Override
    public String toString() {
        return "RecipeResponseTest{" +
                "id=" + id +
                ", recipeName='" + recipeName + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }
}
