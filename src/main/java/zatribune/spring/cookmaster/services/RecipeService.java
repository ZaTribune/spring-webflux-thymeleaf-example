package zatribune.spring.cookmaster.services;

import zatribune.spring.cookmaster.data.entities.Recipe;

import java.util.Set;

//this is an abstraction
public interface RecipeService {
    Set<Recipe> getRecipes();
}
