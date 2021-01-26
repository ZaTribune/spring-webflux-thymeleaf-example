package zatribune.spring.cookmaster.services;

import zatribune.spring.cookmaster.commands.RecipeCommand;
import zatribune.spring.cookmaster.data.entities.Recipe;

import java.util.Optional;
import java.util.Set;

//this is an abstraction
public interface RecipeService {
    Set<Recipe> getAllRecipes();
    Recipe getRecipeById(String id);
    RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand);
    void deleteRecipe(Recipe recipe);
    void deleteRecipeById(String id);
}
