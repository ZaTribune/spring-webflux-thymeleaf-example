package com.tribune.demo.km.data.repository;

import org.bson.types.ObjectId;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.data.entity.Recipe;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, ObjectId> {
   Mono<Recipe> findByTitle(String title);
   @NonNull Flux<Recipe> findAll();
}
