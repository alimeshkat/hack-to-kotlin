package nl.rabobank.kotlinmovement.recipes.data;


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
    private Long id;

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

        if (getId() == null || that.getId() == null) return false;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        if (getId() == null) {
            return super.hashCode();
        } else {
            return getId().hashCode();
        }
    }
}

