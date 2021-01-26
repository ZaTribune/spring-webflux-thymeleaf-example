package zatribune.spring.cookmaster.services;

import zatribune.spring.cookmaster.data.entities.Ingredient;
import zatribune.spring.cookmaster.data.entities.Recipe;

import java.util.Optional;
import java.util.Set;

public interface IngredientService {
   Optional<Ingredient> getIngredientById(String id);
   Set<Ingredient>getIngredientsByRecipe(Recipe recipe);
   void deleteIngredient(Ingredient ingredient);
   void deleteIngredientById(String id);
}
