package com.tribune.demo.km.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.command.RecipeCommand;
import com.tribune.demo.km.data.entity.Recipe;

//this is an abstraction
public interface RecipeService {
    Flux<Recipe> getAllRecipes();
    Mono<Recipe> getRecipeById(String id);
    Mono<RecipeCommand>getRecipeCommandById(String id);
    Mono<Recipe> getRecipeByTitle(String description);
    Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand);
    Mono<Void> deleteRecipe(Recipe recipe);
    Mono<Void> deleteRecipeById(String id);
}
