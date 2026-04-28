package com.tribune.demo.km.data.repository;

import org.bson.types.ObjectId;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.data.entity.UnitMeasure;

public interface UnitMeasureReactiveRepository extends ReactiveMongoRepository<UnitMeasure, ObjectId> {
    Mono<UnitMeasure> findByDescription(String description);
    @NonNull Flux<UnitMeasure> findAll();
}
