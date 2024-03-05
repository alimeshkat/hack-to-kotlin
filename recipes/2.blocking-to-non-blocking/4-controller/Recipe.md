# Controller

We are almost there! The last steps are:
- convert the `Controller` layer to use the non-blocking `R2DBC` repositories,
- and make the integration tests non-blocking as well.

## Recipe

1) Inject the `RecipesService` from the `service.r2dbc` package into the `RecipesController` class.
2) Change the return type of the `getRecipes` method to `Flow<RecipeResponse>`.
3) Add the `suspend` modifier to the `createRecipes`, `getRecipe`,  `updateRecipe` and `deleteRecipes` function.
4) In the next section we will do a clean-up; remove `JPA` related code.

[Go to next section](../5-clean-up/Recipe.md)     