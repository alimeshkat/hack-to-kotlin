package nl.rabobank.kotlinmovement.recipes.test.model;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecipesErrorResponseTest that = (RecipesErrorResponseTest) o;

        return getMessage() != null ? getMessage().equals(that.getMessage()) : that.getMessage() == null;
    }

    @Override
    public int hashCode() {
        return getMessage() != null ? getMessage().hashCode() : 0;
    }
}
