package nl.rabobank.kotlinmovement.recipes.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientsRepository extends JpaRepository<IngredientsEntity, Long> {
}
