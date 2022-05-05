package nl.rabobank.kotlinmovement.recipes.data.repository;

import nl.rabobank.kotlinmovement.recipes.data.entity.IngredientsEntity;
import org.springframework.data.repository.CrudRepository;

public interface IngredientsRepository extends CrudRepository<IngredientsEntity, Long> {
}
