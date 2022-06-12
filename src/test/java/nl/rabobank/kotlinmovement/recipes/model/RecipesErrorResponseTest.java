package nl.rabobank.kotlinmovement.recipes.model;

public class RecipesErrorResponseTest {
    private final String message;

    public RecipesErrorResponseTest(){
        this(null);
    }
    public RecipesErrorResponseTest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
