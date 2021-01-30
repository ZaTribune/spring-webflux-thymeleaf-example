package zatribune.spring.cookmaster.data.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zatribune.spring.cookmaster.data.entities.UnitMeasure;

public interface UnitMeasureReactiveRepository extends ReactiveMongoRepository<UnitMeasure,String> {
    Mono<UnitMeasure> findByDescription(String description);
    Flux<UnitMeasure>findAll();
}
