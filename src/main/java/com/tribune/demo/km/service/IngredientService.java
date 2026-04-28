package com.tribune.demo.km.service;

import reactor.core.publisher.Mono;
import com.tribune.demo.km.command.IngredientCommand;
import com.tribune.demo.km.data.entity.Ingredient;

public interface IngredientService {
   Mono<IngredientCommand> getIngredientById(String id);
   Mono<Void> deleteIngredient(Ingredient ingredient);
   Mono<Void> deleteIngredientById(String id);
}
