package nl.rabobank.kotlinmovement.recipes.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientRequest {
    private Long id;

    @NonNull
    private String name;
    @NonNull
    private String type;
    @NonNull
    private int weight;
}

