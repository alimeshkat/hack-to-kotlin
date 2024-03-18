package nl.alimeshkat.recipes.data;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "ingredients")
public final class IngredientsEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "recipes_id", nullable = false)
    private RecipesEntity recipes;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientId;

    @NotNull
    private String name;
    @NotNull
    private String type;
    @NotNull
    private Integer weight;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IngredientsEntity that = (IngredientsEntity) o;

        if (getIngredientId() == null || that.getIngredientId() == null) return false;

        return getIngredientId().equals(that.getIngredientId());
    }

    @Override
    public int hashCode() {
        if (getIngredientId() == null) {
            return super.hashCode();
        } else {
            return getIngredientId().hashCode();
        }
    }
}

