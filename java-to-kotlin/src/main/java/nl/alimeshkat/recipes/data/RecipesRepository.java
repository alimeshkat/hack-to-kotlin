package nl.alimeshkat.recipes.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipesRepository extends JpaRepository<RecipesEntity, Long> {}
