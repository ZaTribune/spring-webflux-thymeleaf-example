package com.tribune.demo.km.data.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.data.entity.Category;

public interface CategoryReactiveRepository extends ReactiveMongoRepository<Category, ObjectId> {
    Mono<Category> findByDescription(String description);
}
