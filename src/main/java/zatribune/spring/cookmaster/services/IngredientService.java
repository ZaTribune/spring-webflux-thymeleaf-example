package zatribune.spring.cookmaster.services;

import reactor.core.publisher.Mono;
import zatribune.spring.cookmaster.commands.IngredientCommand;
import zatribune.spring.cookmaster.data.entities.Ingredient;

public interface IngredientService {
   Mono<IngredientCommand> getIngredientById(String id);
   void deleteIngredient(Ingredient ingredient);
   void deleteIngredientById(String id);
}
