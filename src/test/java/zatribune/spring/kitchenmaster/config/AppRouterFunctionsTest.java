package zatribune.spring.kitchenmaster.config;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;
import zatribune.spring.kitchenmaster.data.entities.Recipe;
import zatribune.spring.kitchenmaster.services.RecipeService;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppRouterFunctionsTest {

    WebTestClient webTestClient;

    @Mock
    RecipeService recipeService;

    @BeforeEach
    void setUp() {

        AppRouterFunctions appRouterFunctions = new AppRouterFunctions();

        RouterFunction<?> routerFunction = appRouterFunctions.routeGetAllRecipes(recipeService);

        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();

    }

    @Test
    void routeGetAllRecipes() {
        Recipe r1 = new Recipe();
        r1.setId(new ObjectId());
        r1.setTitle("title1");
        Recipe r2 = new Recipe();
        r2.setId(new ObjectId());
        r2.setTitle("title2");
        Recipe r3 = new Recipe();
        r3.setId(new ObjectId());
        r3.setTitle("title3");
        when(recipeService.getAllRecipes()).thenReturn(Flux.just(r1, r2, r3));

        webTestClient.get().uri("/api/recipes")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Recipe.class);

    }
}