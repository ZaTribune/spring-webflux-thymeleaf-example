package zatribune.spring.kitchenmaster.data.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.data.entities.Recipe;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe,String> {
   Mono<Recipe>findByTitle(String title);
   Flux<Recipe> findAll();
}
