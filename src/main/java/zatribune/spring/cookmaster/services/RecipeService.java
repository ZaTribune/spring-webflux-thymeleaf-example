package zatribune.spring.cookmaster.services;

import zatribune.spring.cookmaster.data.entities.Recipe;

public interface RecipeService {
    Iterable<Recipe>getRecipes();
}
