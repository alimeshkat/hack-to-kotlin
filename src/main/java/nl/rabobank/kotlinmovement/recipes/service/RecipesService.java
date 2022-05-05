package nl.rabobank.kotlinmovement.recipes.service;

import lombok.AllArgsConstructor;
import nl.rabobank.kotlinmovement.recipes.data.entity.IngredientsEntity;
import nl.rabobank.kotlinmovement.recipes.data.entity.RecipesEntity;
import nl.rabobank.kotlinmovement.recipes.data.repository.IngredientsRepository;
import nl.rabobank.kotlinmovement.recipes.data.repository.RecipesRepository;
import nl.rabobank.kotlinmovement.recipes.domain.RecipeRequest;
import nl.rabobank.kotlinmovement.recipes.domain.RecipeResponse;
import nl.rabobank.kotlinmovement.recipes.exeption.ResourceNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static nl.rabobank.kotlinmovement.recipes.service.RecipesMapper.toIngredientsEntity;
import static nl.rabobank.kotlinmovement.recipes.service.RecipesMapper.toRecipeEntity;
import static nl.rabobank.kotlinmovement.recipes.service.RecipesMapper.toRecipeResponse;

@Service
@AllArgsConstructor
public class RecipesService {
    private final RecipesRepository recipeRepository;
    private final IngredientsRepository ingredientsRepository;

    @Transactional
    public RecipeResponse getRecipe(Long id) {
        var recipe = recipeRepository.findById(id);
        return recipe.map(r -> toRecipeResponse(r, r.getIngredients()))
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public List<RecipeResponse> getRecipes() {
        return StreamSupport.stream(recipeRepository.findAll().spliterator(), false)
                .map(r -> toRecipeResponse(r, r.getIngredients())).collect(Collectors.toList());
    }

    @Transactional
    public RecipeResponse saveRecipe(RecipeRequest recipeRequest) {
        final RecipesEntity recipe = toRecipeEntity(recipeRequest);
        var recipes = recipeRepository.save(recipe);
        var ingredients = saveIngredients(recipeRequest, recipes);
        return toRecipeResponse(recipes, ingredients);
    }

    @Transactional
    public RecipeResponse updateOrCreateRecipe(Long id, RecipeRequest recipeRequest) {
        RecipesEntity recipes = updateOrCreateRecipes(id, recipeRequest);
        final Set<IngredientsEntity> ingredients = saveIngredients(recipeRequest, recipes);
        return toRecipeResponse(recipes, ingredients);
    }
    @Transactional
    public void deleteRecipe(Long id) {
        try {
            recipeRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException();
        }
    }

    private RecipesEntity updateOrCreateRecipes(Long id, RecipeRequest recipeRequest) {
        return recipeRepository.findById(id).map(it -> {
            it.setRecipeName(recipeRequest.getRecipeName());
            it.getIngredients().clear();
            return it;
        }).orElse(recipeRepository.save(toRecipeEntity(recipeRequest)));
    }

    private Set<IngredientsEntity> saveIngredients(RecipeRequest recipeRequest, RecipesEntity recipe) {
        final Set<IngredientsEntity> ingredients = toIngredientsEntity(recipeRequest, recipe);
        return StreamSupport.stream(
                        ingredientsRepository
                                .saveAll(ingredients)
                                .spliterator(), false
                ).collect(Collectors.toSet());
    }

}
