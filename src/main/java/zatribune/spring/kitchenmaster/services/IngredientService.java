package zatribune.spring.kitchenmaster.services;

import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.commands.IngredientCommand;
import zatribune.spring.kitchenmaster.data.entities.Ingredient;

public interface IngredientService {
   Mono<IngredientCommand> getIngredientById(String id);
   void deleteIngredient(Ingredient ingredient);
   void deleteIngredientById(String id);
}
