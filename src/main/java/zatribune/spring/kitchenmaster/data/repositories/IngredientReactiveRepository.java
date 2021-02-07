package zatribune.spring.kitchenmaster.data.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.data.entities.Ingredient;

public interface IngredientReactiveRepository extends ReactiveMongoRepository<Ingredient,String> {
    Mono<Ingredient>findByDescription(String description);
}
