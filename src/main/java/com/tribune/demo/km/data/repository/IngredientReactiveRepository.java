package com.tribune.demo.km.data.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.data.entity.Ingredient;

public interface IngredientReactiveRepository extends ReactiveMongoRepository<Ingredient, ObjectId> {
    Mono<Ingredient> findByDescription(String description);
}
