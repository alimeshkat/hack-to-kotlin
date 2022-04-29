package nl.rabobank.kotlinmovement.recipes.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRequest {
    @NonNull
    private String recipeName;
    @NonNull
    private Set<IngredientRequest> ingredients;
}
