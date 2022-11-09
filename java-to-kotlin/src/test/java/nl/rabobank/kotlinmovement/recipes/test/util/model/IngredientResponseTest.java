package nl.rabobank.kotlinmovement.recipes.test.util.model;

public class IngredientResponseTest {
    private final Long id;
    private final String name;
    private final IngredientTypeTest type;
    private final int weight;

    public IngredientResponseTest(){
        this(null, null, null, 0);
    }
    public IngredientResponseTest(Long id, String name, IngredientTypeTest type, int weight) {
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

    public IngredientTypeTest getType() {
        return type;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "IngredientResponseTest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", weight=" + weight +
                '}';
    }
}
