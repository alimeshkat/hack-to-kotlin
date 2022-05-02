package nl.rabobank.kotlinmovement.recipes.service.repository;

import nl.rabobank.kotlinmovement.recipes.service.entity.RecipesEntity;
import org.springframework.data.repository.CrudRepository;

public interface RecipesRepository extends CrudRepository<RecipesEntity, Long> {
}
