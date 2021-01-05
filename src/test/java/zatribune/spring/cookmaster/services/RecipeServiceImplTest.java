package zatribune.spring.cookmaster.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import zatribune.spring.cookmaster.commands.RecipeCommand;
import zatribune.spring.cookmaster.data.entities.Difficulty;
import zatribune.spring.cookmaster.data.entities.Recipe;
import zatribune.spring.cookmaster.data.repositories.RecipeRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    RecipeServiceImpl recipeService;

    private final Long idRecipe=14L;
    private final String title="a dummy recipe title";

    //we're using it to test business logic inside service layer

    @BeforeEach
    public void setUp() {
    }


    @Test
    public void getRecipes() {

        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        HashSet<Recipe> mockSet = new HashSet<>();
        mockSet.add(recipe);
        //when the mock is asked to get this data -----> return this data
        when(recipeService.getAllRecipes()).thenReturn(mockSet);

        //ask for data
        Set<Recipe> recipes = recipeService.getAllRecipes();
        log.debug(""+recipes.iterator().next().getTitle());
        assertEquals(recipes.size(), mockSet.size());
        //to verify it's called once
        verify(recipeService,times(1)).getAllRecipes();

    }
    @Test
    public void getRecipeByIdTest(){
        Recipe recipe=new Recipe();
        recipe.setId(idRecipe);
        Optional<Recipe>optionalRecipe=Optional.of(recipe);

        when(recipeService.getRecipeById(anyLong())).thenReturn(optionalRecipe);

        Optional<Recipe> returnedRecipe=recipeService.getRecipeById(idRecipe);

        assertNotNull(returnedRecipe);
        returnedRecipe.ifPresent(value -> assertEquals(idRecipe, value.getId()));

        verify(recipeService,times(1)).getRecipeById(anyLong());
        verify(recipeService,never()).getAllRecipes();


    }

}