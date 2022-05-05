package nl.rabobank.kotlinmovement.recipes.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRequest {
    @NotBlank
    private String recipeName;
    @NotEmpty
    @NotNull
    private Set<IngredientRequest> ingredients;
}
