package nl.rabobank.kotlinmovement.recipes.data;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecipesRepository extends PagingAndSortingRepository<RecipesEntity, Long> {
}
