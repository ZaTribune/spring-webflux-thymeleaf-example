package zatribune.spring.cookmaster.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zatribune.spring.cookmaster.data.entities.Recipe;
import zatribune.spring.cookmaster.data.repositories.RecipeRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


// this is a concretion
@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService{
   private final RecipeRepository recipeRepository;

   @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Set<Recipe> getRecipes() {
       log.debug("I'm in the RecipeService");
       Set<Recipe> recipeSet= StreamSupport.stream(recipeRepository.findAll().spliterator(),false)
               .collect(Collectors.toSet());

        return recipeSet;
    }
}
