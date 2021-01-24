package zatribune.spring.cookmaster.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import zatribune.spring.cookmaster.converters.RecipeCommandToRecipe;
import zatribune.spring.cookmaster.data.entities.Recipe;
import zatribune.spring.cookmaster.data.repositories.RecipeRepository;
import zatribune.spring.cookmaster.exceptions.MyNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @InjectMocks
    RecipeServiceImpl recipeService;
    @Mock
    RecipeRepository recipeRepository;
    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    private Long idRecipe;
    private String title;

    //we're using it to test business logic inside service layer

    @BeforeEach
    public void setUp() {
        idRecipe=14L;
        title="a dummy recipe title";
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
        verify(recipeRepository,times(1)).findAll();

    }
    @Test
    public void getRecipeByIdTest(){
        Recipe recipe=new Recipe();
        recipe.setId(idRecipe);

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        Recipe returnedRecipe=recipeService.getRecipeById(idRecipe);

        assertNotNull(returnedRecipe);
        assertEquals(idRecipe, returnedRecipe.getId());

        verify(recipeRepository,times(1)).findById(anyLong());
        verify(recipeRepository,never()).findAll();
    }

    @Test
    public void getRecipeByIdNotFound(){
        Optional<Recipe> recipeOptional = Optional.empty();
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        Exception exception = assertThrows(MyNotFoundException.class, () -> {
            recipeService.getRecipeById(15L);
        });
        assertTrue(exception.getMessage().contains("Recipe not found"));
    }

}