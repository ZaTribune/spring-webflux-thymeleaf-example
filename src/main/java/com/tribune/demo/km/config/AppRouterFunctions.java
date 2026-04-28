package com.tribune.demo.km.config;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.data.entity.Recipe;
import com.tribune.demo.km.service.RecipeService;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;


@Slf4j
@Configuration
public class AppRouterFunctions {

    // just an example to spool up a simple microservice

    @Bean
    public RouterFunction<?> routeGetAllRecipes(RecipeService recipeService) {
        return RouterFunctions.route(GET("/api/recipes"), new HandlerFunction<>() {
            @Override
            @NonNull
            public Mono<ServerResponse> handle(@NonNull ServerRequest serverRequest) {
                // Extract pagination parameters from query string
                int page = serverRequest.queryParam("page")
                        .map(Integer::parseInt)
                        .orElse(0);
                int size = serverRequest.queryParam("size")
                        .map(Integer::parseInt)
                        .orElse(12);

                log.debug("Router: GET /api/recipes with pagination - page={}, size={}", page, size);

                return ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(recipeService.getAllRecipes()
                                .skip((long) page * size)
                                .take(size), Recipe.class);
            }
        });
    }
}
