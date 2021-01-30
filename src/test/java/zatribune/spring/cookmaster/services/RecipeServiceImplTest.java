package zatribune.spring.cookmaster.services;

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
import zatribune.spring.cookmaster.commands.RecipeCommand;
import zatribune.spring.cookmaster.converters.RecipeCommandToRecipe;
import zatribune.spring.cookmaster.converters.RecipeToRecipeCommand;
import zatribune.spring.cookmaster.data.entities.Recipe;
import zatribune.spring.cookmaster.data.repositories.RecipeReactiveRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;


    private ObjectId idRecipe;
    private String title;

    //we're using it to test business logic inside service layer

    @BeforeEach
    public void setUp() {
        idRecipe=new ObjectId();
        title="a dummy recipe title";
    }


    @Test
    public void getRecipes() {
        Recipe recipe = new Recipe();
        recipe.setId(idRecipe);
        recipe.setTitle(title);
        //when the mock is asked to get this data -----> return this data
        when(recipeService.getAllRecipes()).thenReturn(Flux.just(recipe));
        //ask for data
        Flux<Recipe> recipes = recipeService.getAllRecipes();
        assertNotNull(recipes);
        assertEquals(1,recipes.count().block());
        //to verify it's called once
        verify(recipeRepository,times(1)).findAll();

    }
    @Test
    public void getRecipeByIdTest(){
        Recipe recipe=new Recipe();
        recipe.setId(idRecipe);

        when(recipeService.getRecipeById(anyString())).thenReturn(Mono.just(recipe));

        Recipe returnedRecipe=recipeService.getRecipeById(idRecipe.toString()).block();

        assertNotNull(returnedRecipe);
        assertEquals(idRecipe, returnedRecipe.getId());

        verify(recipeRepository,times(1)).findById(anyString());
        verify(recipeRepository,never()).findAll();
    }

    @Test
    public void getRecipeCommandByIdTest(){
        Recipe recipe=new Recipe();
        recipe.setId(idRecipe);
        when(recipeService.getRecipeById(anyString())).thenReturn(Mono.just(recipe));

        RecipeCommand recipeCommand=new RecipeCommand();
        recipeCommand.setId(idRecipe.toString());
        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        RecipeCommand returnedRecipe=recipeService.getRecipeCommandById(idRecipe.toString()).block();

        assertNotNull(returnedRecipe);
        assertEquals(idRecipe.toString(), returnedRecipe.getId());

        verify(recipeRepository,times(1)).findById(anyString());
        verify(recipeRepository,never()).findAll();
    }

    @Test
    void deleteRecipeByID(){
        recipeService.deleteRecipeById(idRecipe.toString());
        verify(recipeRepository,times(1)).deleteById(idRecipe.toString());
    }

}