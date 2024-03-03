# Service

The service layer is still using the blocking `JPA` repositories. We will convert the `Service` layer to use the non-blocking `R2DBC` repositories.

## Recipe

1) CREATE a new package in the `service` package called `r2dbc`.
2) CREATE a new class called `RecipesService` in the `r2dbc` package, and annotate it with `@Service`.
3) Inject:
   1) `RecipesRepository` from the `data.r2dbc` package
   2) `IngredientsRepository` from the `data.r2dbc` package
   3) `RecipesAndIngredientsRepository`from the `data.r2dbc` package
4) Let's start with the adding the method for fetching all the `RecipesEntities` and `IngredientsEntities` from the `R2DBC` repositories, and map them to `RecipeResponse`.
   1) In the `RecipesService` class, add the following functions:
      ```kotlin
           @get:Transactional
           val recipes: Flow<RecipeResponse> 
                 get() = recipesAndIngredientsRepository.findAll()
                             .mapNotNull { r -> r.ingredients.let { toRecipeResponse(r, r.ingredients) } }

   
          //This is the mapper function was declared previously in the RecipeMapper.kt file, but for the sake of this exercise we will declare it here.
          private fun toRecipeResponse(
            recipes: nl.rabobank.kotlinmovement.recipes.data.r2dbc.RecipesEntity,
            ingredientsEntities: Set<nl.rabobank.kotlinmovement.recipes.data.r2dbc.IngredientsEntity>
            ): RecipeResponse =
               RecipeResponse(
                 checkNotNull(recipes.recipeId),
                 recipes.recipeName,
                 ingredientsEntities.map { ingredient ->
                    IngredientResponse(
                        ingredient.ingredientId,
                        ingredient.name,
                        IngredientType.valueOf(ingredient.type),
                        ingredient.weight
                    )
                }.toSet()
            )
      ```
5) Now we add the method for fecthing a single `RecipesEntity` and `IngredientsEntities` from the `R2DBC` repositories, and map them to `RecipeResponse`.
   1) In the `RecipesService` class, add the following function:
      ```kotlin
          @Transactional
          suspend fun getRecipe(id: Long): RecipeResponse? = recipesAndIngredientsRepository.findByIdOrNull(id)
             ?.let { recipesEntity ->
               checkNotNull(recipesEntity.ingredients)
               toRecipeResponse(recipesEntity, recipesEntity.ingredients)
          }
          ?: throw ResourceNotFoundException("Recipe $id not found")
      ```