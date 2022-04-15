package nl.rabobank.kotlinmovement.pizzarecipes;


import lombok.Data;

@Data
public class PizzaRecipeRequest {
    private final Long id;
    private final String recipeName;
}
