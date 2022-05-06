package nl.rabobank.kotlinmovement.recipes.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientRequestJ {
    @NotBlank
    private String name;
    @NotBlank
    private IngredientTypeJ type;
    @NotBlank
    private int weight;
}

