package nl.rabobank.kotlinmovement.recipes.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientResponseJ {
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private IngredientTypeJ type;
    @NonNull
    private int weight;
}
