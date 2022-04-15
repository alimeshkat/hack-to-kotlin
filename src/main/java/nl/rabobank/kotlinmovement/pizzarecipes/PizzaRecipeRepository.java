package nl.rabobank.kotlinmovement.pizzarecipes;

import org.springframework.data.repository.CrudRepository;

public interface PizzaRecipeRepository extends CrudRepository<PizzaRecipeEntity, Long> {
}
