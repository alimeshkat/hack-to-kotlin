package nl.rabobank.kotlinmovement.recipes.service.repository;

import nl.rabobank.kotlinmovement.recipes.domain.entity.IngredientsEntity;
import org.springframework.data.repository.CrudRepository;

public interface IngredientsRepository extends CrudRepository<IngredientsEntity, Long> {
}
