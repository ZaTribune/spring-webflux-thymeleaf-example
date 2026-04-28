package com.tribune.demo.km.service;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.command.RecipeCommand;
import com.tribune.demo.km.converter.RecipeToRecipeCommand;
import com.tribune.demo.km.data.entity.Recipe;
import com.tribune.demo.km.data.repository.RecipeReactiveRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @InjectMocks
    RecipeServiceImpl recipeService;
    @Mock
    RecipeReactiveRepository recipeRepository;
    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;


    private ObjectId idRecipe;
    private String title;

    //we're using it to test business logic inside service layer

    @BeforeEach
    public void setUp() {
        idRecipe = new ObjectId();
        title = "a dummy recipe title";
    }


    @Test
    public void getRecipes() {
        Recipe recipe = new Recipe();
        recipe.setId(idRecipe);
        recipe.setTitle(title);
        //when the mock repository is asked to get this data -----> return this data
        when(recipeRepository.findAll()).thenReturn(Flux.just(recipe));
        //ask the service for data
        Flux<Recipe> recipes = recipeService.getAllRecipes();
        assertNotNull(recipes);
        assertEquals(1, recipes.count().block());
        //to verify the repository was called once
        verify(recipeRepository, times(1)).findAll();

    }

    @Test
    public void getRecipeByIdTest() {
        Recipe recipe = new Recipe();
        recipe.setId(idRecipe);
        //Mock the repository to return the recipe
        when(recipeRepository.findById(idRecipe)).thenReturn(Mono.just(recipe));

        //Call the service method
        Recipe returnedRecipe = recipeService.getRecipeById(idRecipe.toString()).block();

        assertNotNull(returnedRecipe);
        assertEquals(idRecipe, returnedRecipe.getId());

        verify(recipeRepository, times(1)).findById(idRecipe);
        verify(recipeRepository, never()).findAll();
    }

    @Test
    public void getRecipeCommandByIdTest() {
        Recipe recipe = new Recipe();
        recipe.setId(idRecipe);
        //Mock the repository to return the recipe
        when(recipeRepository.findById(idRecipe)).thenReturn(Mono.just(recipe));

        //Mock the converter
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(idRecipe.toString());
        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        //Call the service method
        RecipeCommand returnedRecipe = recipeService.getRecipeCommandById(idRecipe.toString()).block();

        assertNotNull(returnedRecipe);
        assertEquals(idRecipe.toString(), returnedRecipe.getId());

        verify(recipeRepository, times(1)).findById(idRecipe);
        verify(recipeToRecipeCommand, times(1)).convert(any());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    void deleteRecipeByID() {
        //Mock the repository delete
        when(recipeRepository.deleteById(idRecipe)).thenReturn(Mono.empty());

        //Call the service method
        recipeService.deleteRecipeById(idRecipe.toString()).block();

        //Verify the repository delete was called with the correct ID
        verify(recipeRepository, times(1)).deleteById(idRecipe);
    }

}