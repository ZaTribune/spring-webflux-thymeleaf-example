package zatribune.spring.cookmaster.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import reactor.core.publisher.Flux;
import zatribune.spring.cookmaster.commands.RecipeCommand;
import zatribune.spring.cookmaster.converters.RecipeToRecipeCommand;
import zatribune.spring.cookmaster.data.entities.Recipe;
import zatribune.spring.cookmaster.data.entities.UnitMeasure;
import zatribune.spring.cookmaster.exceptions.MyNotFoundException;
import zatribune.spring.cookmaster.services.RecipeService;
import zatribune.spring.cookmaster.services.RecipeServiceImpl;
import zatribune.spring.cookmaster.services.UnitMeasureService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class RecipesControllerTest {
    //When using Mockito, all arguments have to be provided by matchers.
    //the mockito methods only work on objects annotated with @Mock
    @Mock
    RecipeServiceImpl recipeService;
    @Mock
    UnitMeasureService unitMeasureService;
    @Mock
    Model model;
    // to fix unchecked assignment problems
    @Captor
    ArgumentCaptor<List<Recipe>> captor;
    @InjectMocks
    RecipesController recipesController;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        //recipesController=new RecipesController(recipeService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(recipesController)
                // we have to specify it for exception handling
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void getIndexPage() throws Exception {
        //webAppContextSetup will bring the Spring context therefore our test will no longer be a unit testing
        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/homeRecipes"));

    }

    @Test
    void getSearchRecipesPage() { // this is an example of test-driven-development {given-when-then}

        //************ given ************
        String expectedURL = "recipes/searchRecipes";
        Recipe recipe1 = new Recipe();
        recipe1.setTitle("recipe 1");
        Recipe recipe2 = new Recipe();
        recipe2.setTitle("recipe 2");

        //************ when ************
        when(recipeService.getAllRecipes()).thenReturn(Flux.just(recipe1,recipe2));

        //************ then ************
        assertEquals(expectedURL, recipesController.searchRecipes(model));
        verify(recipeService, times(1)).getAllRecipes();
        //this verifies that addAttribute() is called once.
        //the captor is to make sure that the argument passed to the function is the right one/type
        verify(model, times(1)).addAttribute(eq("recipes"), captor.capture());
        List<Recipe>list=captor.getValue();
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    public void getRecipeByIdNotFound() throws Exception {
        when(recipeService.getRecipeById(anyString())).thenThrow(MyNotFoundException.class);
        mockMvc.perform(get("/showRecipe/15"))
               .andExpect(status().isNotFound())
               .andExpect(view().name("errors/404"));
        //side by side with the custom annotated exception class
        //and the double annotated exception handler function on the controller
    }

    @Test
    public void postNewRecipeForm() throws Exception {

        //when(recipeService.saveRecipeCommand(any())).thenReturn(recipeCommand);
        mockMvc.perform(post("/updateOrSaveRecipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id","20")
                .param("title","xx")
                .param("directions","")
                .param("prepTime","0")
                .param("cookTime","0")
                 )
                .andExpect(status().isOk())
        .andReturn();

    }

}