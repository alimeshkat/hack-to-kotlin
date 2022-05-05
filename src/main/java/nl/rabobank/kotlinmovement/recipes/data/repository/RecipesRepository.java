package nl.rabobank.kotlinmovement.recipes.data.repository;

import nl.rabobank.kotlinmovement.recipes.data.entity.RecipesEntity;
import org.springframework.data.repository.CrudRepository;

public interface RecipesRepository extends CrudRepository<RecipesEntity, Long> {
}
