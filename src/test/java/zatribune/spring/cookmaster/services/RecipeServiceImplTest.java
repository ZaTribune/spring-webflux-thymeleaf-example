package zatribune.spring.cookmaster.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import zatribune.spring.cookmaster.data.entities.Recipe;
import zatribune.spring.cookmaster.data.repositories.RecipeRepository;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    //we're using it to test business logic inside service layer
    @Mock
    // it needs to be mocked cause it has third party dependencies
    RecipeRepository recipeRepository;

    @BeforeEach
    public void setUp() {
        //MockitoAnnotations.initMocks(this);
        MockitoAnnotations.openMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository);

    }


    @Test
    public void getRecipes() {

        Recipe recipe = new Recipe();
        recipe.setTitle("mock1 recipe");

        HashSet<Recipe> mockSet = new HashSet<>();
        mockSet.add(recipe);

        //when the mock is asked to get this data -----> return this data
        when(recipeService.getRecipes()).thenReturn(mockSet);

        //ask for data
        Set<Recipe> recipes = recipeService.getRecipes();
        log.debug(""+recipes.iterator().next().getTitle());
        assertEquals(recipes.size(), mockSet.size());
        //to verify it's called once
        verify(recipeRepository,times(1)).findAll();

    }

}