package nl.alimeshkat.recipes.model;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public final class RecipesErrorResponse {
    @NotNull
    private final String message;
}
