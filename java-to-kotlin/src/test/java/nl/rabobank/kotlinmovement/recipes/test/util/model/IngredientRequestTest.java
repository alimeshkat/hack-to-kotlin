package nl.rabobank.kotlinmovement.recipes.test.util.model;

public class IngredientRequestTest {

    private final String name;
    private final IngredientTypeTest type;
    private final Integer weight;

    public IngredientRequestTest() {
        this(null,null,0);
    }

    public IngredientRequestTest(String name, IngredientTypeTest type, Integer weight) {
        this.name = name;
        this.type = type;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public IngredientTypeTest getType() {
        return type;
    }

    public Integer getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "IngredientRequestTest{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", weight=" + weight +
                '}';
    }
}
