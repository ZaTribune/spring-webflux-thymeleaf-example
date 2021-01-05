package zatribune.spring.cookmaster.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zatribune.spring.cookmaster.data.entities.Recipe;
import zatribune.spring.cookmaster.services.RecipeService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class RecipeInfoControllerTest {

    @Mock
    RecipeService recipeService;

    RecipeInfoController recipeInfoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeInfoController =new RecipeInfoController(recipeService);
        //MockMvc mockMvc= MockMvcBuilders.standaloneSetup()
    }

    @Test
    void getRecipePage() throws Exception {
        //***** given

        Recipe recipe=new Recipe();
        recipe.setId(1L);
        Optional<Recipe>optionalRecipe=Optional.of(recipe);

        when(recipeService.getRecipeById(anyLong()))
                .thenReturn(optionalRecipe);


        MockMvc mockMvc=MockMvcBuilders.standaloneSetup(recipeInfoController).build();
        mockMvc.perform(get("/recipe/show/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipe/show"));


    }
}