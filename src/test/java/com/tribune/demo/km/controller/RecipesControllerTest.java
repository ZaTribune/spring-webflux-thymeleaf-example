package com.tribune.demo.km.controller;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.command.RecipeCommand;
import com.tribune.demo.km.data.entity.Recipe;
import com.tribune.demo.km.service.RecipeService;
import com.tribune.demo.km.service.UnitMeasureService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;


@Slf4j
@ExtendWith(MockitoExtension.class)
@WebFluxTest(RecipesController.class)
@WithMockUser
class RecipesControllerTest {
    // When using Mockito, all arguments have to be provided by matchers
    @MockitoBean
    RecipeService recipeService;
    @MockitoBean
    UnitMeasureService unitMeasureService;

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
        assertFalse(result.isEmpty());
    }

    @Test
    void getSearchRecipesApi() {
        //************ given ************
        Recipe recipe1 = new Recipe();
        recipe1.setTitle("recipe 1");
        Recipe recipe2 = new Recipe();
        recipe2.setTitle("recipe 2");

        //************ when ************
        when(recipeService.getAllRecipes()).thenReturn(Flux.just(recipe1, recipe2));

        String result =
                webTestClient.get().uri("/api/recipes/search?search=recipe").exchange()
                        .expectStatus().isOk()
                        .expectBody(String.class)
                        .returnResult().getResponseBody();

        //************ then ************
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(recipeService, times(1)).getAllRecipes();
    }

    @Test
    public void getRecipeByIdNotFound() {
        String id = new ObjectId().toString();
        when(recipeService.getRecipeCommandById(id)).thenReturn(Mono.empty());
        webTestClient.get().uri("/api/recipes/" + id).exchange()
                .expectStatus().isNotFound();
    }


    @Test
    public void saveOrUpdateRecipeValid() {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setTitle("Hello");
        recipeCommand.setPrepTime(10);
        recipeCommand.setCookTime(15);
        recipeCommand.setDirections("whatever directions");

        when(recipeService.saveRecipeCommand(any())).thenReturn(Mono.just(recipeCommand));

        String jsonBody = "{\"title\":\"hello\",\"prepTime\":10,\"cookTime\":15,\"directions\":\"whatever directions\"}";
        
        String result = webTestClient.post()
                .uri("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .returnResult().getResponseBody();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void saveOrUpdateRecipeViaForm() {
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
        
        String result = webTestClient.mutateWith(csrf())
                .post()
                .uri("/recipes")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(body))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void showRecipeDetail() {
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setTitle("Dummy Title");
        when(recipeService.getRecipeById(id.toString())).thenReturn(Mono.just(recipe));
        String result = webTestClient.get().uri("/recipes/" + id + "/fragment")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getCreateNewRecipeForm() {
        when(unitMeasureService.getAllUnitMeasures()).thenReturn(Flux.empty());
        String result = webTestClient.get().uri("/recipes/new")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getEditRecipeForm() {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(id.toString());
        recipeCommand.setTitle("Dummy Title");
        when(recipeService.getRecipeCommandById(id.toString())).thenReturn(Mono.just(recipeCommand));
        when(unitMeasureService.getAllUnitMeasures()).thenReturn(Flux.empty());
        String result = webTestClient.get().uri("/recipes/" + id + "/edit")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void deleteRecipeApi() {
        when(recipeService.deleteRecipeById(id.toString())).thenReturn(Mono.empty());
        String result = webTestClient.delete().uri("/api/recipes/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

}