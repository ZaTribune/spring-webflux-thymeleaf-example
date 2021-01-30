package zatribune.spring.cookmaster.data.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zatribune.spring.cookmaster.data.entities.Ingredient;
import zatribune.spring.cookmaster.data.entities.Recipe;

public interface IngredientReactiveRepository extends ReactiveMongoRepository<Ingredient,String> {
    Mono<Ingredient>findByDescription(String description);
}
