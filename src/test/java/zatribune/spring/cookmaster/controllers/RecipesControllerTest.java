package zatribune.spring.cookmaster.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import zatribune.spring.cookmaster.data.entities.Recipe;
import zatribune.spring.cookmaster.exceptions.MyNotFoundException;
import zatribune.spring.cookmaster.services.RecipeService;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class RecipesControllerTest {
    //When using Mockito, all arguments have to be provided by matchers.
    //the mockito methods only work on objects annotated with @Mock
    @Mock
    RecipeService recipeService;
    @Mock
    Model model;
    // to fix unchecked assignment problems
    @Captor
    ArgumentCaptor<Set<Recipe>> captor;
    @InjectMocks
    RecipesController recipesController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(recipesController)
                // we have to specify it for exception handling
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void testMockMVC() throws Exception {
        //webAppContextSetup will bring the Spring context therefore our test will no longer be a unit testing
        mockMvc.perform(get("/recipe"))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipe/all"));

    }

    @Test
    void getIndexPage() { // this is an example of test-driven-development {given-when-then}

        //************ given ************
        String expectedIndexString = "/recipe/all";
        Set<Recipe> recipes = new HashSet<>();
        Recipe recipe1 = new Recipe();
        recipe1.setTitle("recipe 1");
        Recipe recipe2 = new Recipe();
        recipe2.setTitle("recipe 2");
        recipes.add(recipe1);
        recipes.add(recipe2);

        //************ when ************
        when(recipeService.getAllRecipes()).thenReturn(recipes);


        //************ then ************
        assertEquals(expectedIndexString, recipesController.getRecipesHomePage(model));
        verify(recipeService, times(1)).getAllRecipes();
        //this verifies that addAttribute() is called once.
        //the captor is to make sure that the argument passed to the function is the right one/type
        verify(model, times(1)).addAttribute(eq("recipes"), captor.capture());
        assertEquals(2, captor.getValue().size());
    }

    @Test
    public void getRecipeByIdNotFound() throws Exception {
        when(recipeService.getRecipeById(anyLong())).thenThrow(MyNotFoundException.class);
        mockMvc.perform(get("/showRecipe/15"))
               .andExpect(status().isNotFound())
               .andExpect(view().name("/errors/404"));
        //side by side with the custom annotated exception class
        //and the double annotated exception handler function on the controller
    }

}