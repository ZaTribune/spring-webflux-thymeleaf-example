package com.tribune.demo.km.service;


import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.command.RecipeCommand;
import com.tribune.demo.km.converter.RecipeCommandToRecipe;
import com.tribune.demo.km.converter.RecipeToRecipeCommand;
import com.tribune.demo.km.data.entity.*;
import com.tribune.demo.km.data.repository.CategoryReactiveRepository;
import com.tribune.demo.km.data.repository.IngredientReactiveRepository;
import com.tribune.demo.km.data.repository.RecipeReactiveRepository;
import com.tribune.demo.km.data.repository.UnitMeasureReactiveRepository;

import java.util.*;


@Slf4j
@Service
public record RecipeServiceImpl (
        RecipeReactiveRepository recipeRepository,
        IngredientReactiveRepository ingredientRepository,
        CategoryReactiveRepository categoryRepository,
        UnitMeasureReactiveRepository unitMeasureRepository,
        RecipeCommandToRecipe recipeCommandToRecipe,
        RecipeToRecipeCommand recipeToRecipeCommand) implements RecipeService {

    @Override
    public Flux<Recipe> getAllRecipes() {
        log.debug("I'm getting recipes from the RecipeService");
        return recipeRepository.findAll();
    }

    @Override
    public Mono<Recipe> getRecipeById(String id) {
        return recipeRepository.findById(new ObjectId(id));
    }

    @Override
    public Mono<RecipeCommand> getRecipeCommandById(String id) {
        return getRecipeById(id).map(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<Recipe> getRecipeByTitle(String title) {
        return recipeRepository.findByTitle(title);
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand) {
        Recipe recipe = recipeCommandToRecipe.convert(recipeCommand);
        return recipeRepository.save(recipe)
                .map(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<Void> deleteRecipe(Recipe recipe) {
        return recipeRepository.delete(recipe);
    }

    @Override
    public Mono<Void> deleteRecipeById(String id) {
        return recipeRepository.deleteById(new ObjectId(id));
    }
}
