package zatribune.spring.kitchenmaster.controllers;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.thymeleaf.exceptions.TemplateInputException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.commands.RecipeCommand;
import zatribune.spring.kitchenmaster.data.entities.Recipe;
import zatribune.spring.kitchenmaster.services.RecipeService;
import zatribune.spring.kitchenmaster.services.UnitMeasureService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;


@Slf4j
@ExtendWith(MockitoExtension.class)
@WebFluxTest(RecipesController.class)
@WithMockUser
class RecipesControllerTest {
    //When using Mockito, all arguments have to be provided by matchers.
    //the mockito methods only work on objects annotated with @Mock
    @MockBean
    RecipeService recipeService;
    @MockBean
    UnitMeasureService unitMeasureService;
    @MockBean
    Model model;
    // to fix unchecked assignment problems
    @Captor
    ArgumentCaptor<Flux<Recipe>> captor;

    @Autowired
    WebTestClient webTestClient;

    ObjectId id;

    @BeforeEach
    void setUp() {
        id = new ObjectId();
    }

    @Test
    void getRecipesHomePage() {
        //webAppContextSetup will bring the Spring context therefore our test will no longer be a unit testing
        List<String> result = webTestClient.get().uri("/recipes").exchange()
                .expectStatus().isOk()
                .returnResult(String.class).getResponseBody().collectList().block();
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    void getSearchRecipesPage() { // this is an example of test-driven-development {given-when-then}

        //************ given ************
        Recipe recipe1 = new Recipe();
        recipe1.setTitle("recipe 1");
        Recipe recipe2 = new Recipe();
        recipe2.setTitle("recipe 2");

        //************ when ************
        when(recipeService.getAllRecipes()).then(invocationOnMock ->
                model.addAttribute("recipes", Flux.just(recipe1, recipe2))).thenReturn(Flux.just(recipe1, recipe2));

        String result =
                webTestClient.get().uri("/searchRecipes").exchange()
                        .expectStatus().isOk()
                        .expectBody(String.class)
                        .returnResult().getResponseBody();

        //************ then ************
        assertNotNull(result);
        assertTrue(result.length()>0);
        verify(recipeService, times(1)).getAllRecipes();

        //this verifies that addAttribute() is called once.
        //the captor is to make sure that the argument passed to the function is the right one/type
        verify(model, times(1)).addAttribute(eq("recipes"), captor.capture());
        List<Recipe> recipes = captor.getValue().collectList().block();
        assertNotNull(recipes);
        assertEquals(2, recipes.size());
    }

    @Test
    public void getRecipeByIdNotFound() {
        String id = new ObjectId().toString();
        String errorMessage = "Recipe not found for id " + id;
        when(recipeService.getRecipeById(id)).thenThrow(new TemplateInputException(errorMessage));
        List<String> result = webTestClient.get().uri("/showRecipe/" + id).exchange()
                .expectStatus().isNotFound()
                .returnResult(String.class)
                .getResponseBody().collectList().block();
        assertNotNull(result);
        System.out.println(String.join("", result));
        assertTrue(String.join("", result).contains(errorMessage));
        //side by side with the custom annotated exception class
        //and the double annotated exception handler function on the controller
    }


    @Test
    public void saveOrUpdateRecipeValid() {


        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setTitle("Hello");
        recipeCommand.setPrepTime(10);
        recipeCommand.setCookTime(15);
        recipeCommand.setDirections("whatever directions");

        when(recipeService.saveRecipeCommand(any())).thenReturn(Mono.just(recipeCommand));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("title", "hello");
        body.add("prepTime", "10");
        body.add("cookTime", "15");
        body.add("directions", "whatever directions");
        //don't use contentType
        String result = webTestClient.mutateWith(csrf())
                .post()
                .uri("/updateOrSaveRecipe", body)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(body))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    public void saveOrUpdateRecipeInValid() {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setTitle("Hello");
        recipeCommand.setPrepTime(10);
        recipeCommand.setCookTime(15);
        recipeCommand.setDirections("whatever directions");

        when(recipeService.saveRecipeCommand(any())).thenReturn(Mono.just(recipeCommand));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("title", "h");
        body.add("prepTime", "0");
        body.add("cookTime", "0");
        body.add("directions", "");
        //don't use contentType
        String result = webTestClient.mutateWith(csrf()).post()
                .uri("/updateOrSaveRecipe", body)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(body))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).returnResult().getResponseBody();
        assertNotNull(result);
        assertTrue(result.length() > 0);
        assertTrue(result.contains("4 error/s"));
    }

    @Test
    void showRecipe() {
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setTitle("Dummy Title");
        when(recipeService.getRecipeById(id.toString())).thenReturn(Mono.just(recipe));
        String result = webTestClient.get().uri("/showRecipe/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    void requestToCreateNewRecipe() {
        //no error will happen but just it's more convenient to have it an EmptyFlux rather than a null value
        when(unitMeasureService.getAllUnitMeasures()).thenReturn(Flux.empty());
        String result = webTestClient.get().uri("/createRecipe")
                .attribute("recipe", Mono.just(new RecipeCommand()))
                .attribute("unitMeasures", unitMeasureService.getAllUnitMeasures())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    void updateRecipe() {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(id.toString());
        recipeCommand.setTitle("Dummy Title");
        when(recipeService.getRecipeCommandById(id.toString())).thenReturn(Mono.just(recipeCommand));
        when(unitMeasureService.getAllUnitMeasures()).thenReturn(Flux.empty());
        String result = webTestClient.get().uri("/updateRecipe/" + id)
                .attribute("recipe", Mono.just(new RecipeCommand()))
                .attribute("unitMeasures", unitMeasureService.getAllUnitMeasures())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    void deleteRecipe() {
        Boolean result = webTestClient.get().uri("/deleteRecipe/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .returnResult().getResponseBody();
        assertNotNull(result);
        assertTrue(result);
    }

}