package nl.rabobank.kotlinmovement.recipes.test.util.model;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientResponseTest that = (IngredientResponseTest) o;
        return weight == that.weight && Objects.equals(id, that.id) && Objects.equals(name, that.name) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, weight);
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
