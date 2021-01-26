package zatribune.spring.cookmaster.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zatribune.spring.cookmaster.commands.RecipeCommand;
import zatribune.spring.cookmaster.converters.RecipeCommandToRecipe;
import zatribune.spring.cookmaster.data.entities.Recipe;
import zatribune.spring.cookmaster.data.repositories.RecipeRepository;
import zatribune.spring.cookmaster.exceptions.MyNotFoundException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


// this is a concretion
@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe converter;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe converter) {
        this.recipeRepository = recipeRepository;
        this.converter = converter;
    }

    @Override
    public Set<Recipe> getAllRecipes() {
        log.debug("I'm getting recipes from the RecipeService");
        return StreamSupport.stream(recipeRepository.findAll().spliterator(), false)
                .collect(Collectors.toSet());
    }

    @Override
    public Recipe getRecipeById(String id) {
        Optional<Recipe>optionalRecipe=recipeRepository.findById(id);
        if (optionalRecipe.isEmpty()){
            throw new MyNotFoundException("Recipe not found for id value: "+id);
        }
        return optionalRecipe.get();
    }

    @Override
    public Recipe getRecipeByTitle(String title) {
        Optional<Recipe>optionalRecipe=recipeRepository.findRecipeByTitle(title);
        if (optionalRecipe.isEmpty()){
            throw new MyNotFoundException("Recipe not found for title value: "+title);
        }
        return optionalRecipe.get();
    }


    @Override
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
        Recipe recipe = converter.convert(recipeCommand);
        System.out.println(recipeCommand.getIngredients());
        if (recipe != null) {
            String id =recipeRepository.save(recipe).getId().toString();
            log.debug("returned recipe id = {}",id);
            log.info("returned recipe id = {}",id);
            recipeCommand.setId(id);
        }
        return recipeCommand;
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        recipeRepository.delete(recipe);
    }

    @Override
    public void deleteRecipeById(String id) {
        recipeRepository.deleteById(id);
    }
}
