package zatribune.spring.kitchenmaster.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.data.entities.Recipe;
import zatribune.spring.kitchenmaster.services.RecipeService;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class AppRouterFunctions {

    // just an example to spool up a simple microservice
    @Bean
    RouterFunction<?> routeGetAllRecipes(RecipeService recipeService) {
        return RouterFunctions.route(GET("/api/recipes"), new HandlerFunction<ServerResponse>() {
            @Override @NonNull
            public Mono<ServerResponse> handle(@NonNull ServerRequest serverRequest) {
                return ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(recipeService.getAllRecipes(), Recipe.class);

            }
        });
    }
}
