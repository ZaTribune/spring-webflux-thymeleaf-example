package zatribune.spring.kitchenmaster.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.commands.RecipeCommand;
import zatribune.spring.kitchenmaster.data.entities.Recipe;

//this is an abstraction
public interface RecipeService {
    Flux<Recipe> getAllRecipes();
    Mono<Recipe> getRecipeById(String id);
    Mono<RecipeCommand>getRecipeCommandById(String id);
    Mono<Recipe> getRecipeByTitle(String description);
    Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand);
    void deleteRecipe(Recipe recipe);
    void deleteRecipeById(String id);
}
