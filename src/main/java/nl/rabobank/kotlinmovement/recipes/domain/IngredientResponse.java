package nl.rabobank.kotlinmovement.recipes.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientResponse {
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private IngredientType type;
    @NonNull
    private int weight;
}
