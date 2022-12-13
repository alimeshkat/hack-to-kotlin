package nl.rabobank.kotlinmovement.recipes.model;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class RecipesErrorResponse {
    @NotNull
    private final String message;
}
