# Service

The service layer is still using the blocking `JPA` repositories. We will convert the `Service` layer to use the non-blocking `R2DBC` repositories.
Because we use Coroutines the changes that we need to make are minimal.

## Recipe

1) CREATE a new package in the `service` package called `r2dbc`.
2) CREATE a new class called `RecipesService` in the `r2dbc` package, and annotate it with `@Service`.
3) Inject:
   1) `RecipesRepository` from the `data.r2dbc` package
   2) `IngredientsRepository` from the `data.r2dbc` package
   3) `RecipesAndIngredientsRepository`from the `data.r2dbc` package
4) Let's start with adding the method for fetching all the `RecipesEntities` and `IngredientsEntities` from the `R2DBC` repositories, and map them to `RecipeResponse`.
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
5) Now we add the method for fetching a single `RecipesEntity` and `IngredientsEntities` from the `R2DBC` repositories, and map them to `RecipeResponse`.
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
      
6) Continue with adding the `save` `updateOrCreateRecipe`  and `deleteRecipe` methods to the `RecipesService` class.
   1) In the `RecipesService` class, add the following functions:
      ```kotlin
          @Transactional
          suspend fun saveRecipe(recipeRequest: RecipeRequest): RecipeResponse? {
             val recipe = toRecipeEntity(recipeRequest)
             val recipes = recipesRepository.save(recipe)
             val ingredients = saveIngredients(recipeRequest, recipes)
             return toRecipeResponse(recipes, ingredients)
          }
       
          @Transactional
          suspend fun updateOrCreateRecipe(id: Long, recipeRequest: RecipeRequest): RecipeResponse? {
             return recipesRepository.findById(id)?.let {
             requireNotNull(recipeRequest.recipeName)
             val recipe = recipesRepository.save(RecipesEntity(it.recipeId, recipeRequest.recipeName))
             val ingredients = saveIngredients(recipeRequest, recipe)
             toRecipeResponse(recipe, ingredients)
          } ?: saveRecipe(recipeRequest)
          }
       
          @Transactional
          suspend fun deleteRecipe(id: Long) {
             recipesRepository.deleteById(id)
          }

          private suspend fun saveIngredients(recipeRequest: RecipeRequest, recipe: RecipesEntity): Set<IngredientsEntity> =
             ingredientsRepository.saveAll(toIngredientsEntity(recipeRequest, recipe)).toSet()


           private fun toIngredientsEntity(recipeRequest: RecipeRequest, recipe: RecipesEntity?): Set<IngredientsEntity> {
               return checkNotNull(recipeRequest.ingredients).map { (name, type, weight): IngredientRequest ->
                  IngredientsEntity(
                       recipeId = recipe?.recipeId,
                       ingredientId = null,
                       name = checkNotNull(name),
                       type = checkNotNull(type).name,
                       weight = checkNotNull(weight),
                   )
               }.toSet()
           }

           private fun toRecipeEntity(recipeRequest: RecipeRequest): RecipesEntity {
               return RecipesEntity(
                   null,
                   checkNotNull(recipeRequest.recipeName),
                   emptySet()
               )
           }
        ```
7) Now the service layer is in place, let's move on to the controller layer and use the new non-blocking service.

[Go to next section](../4-controller/Recipe.md)       

