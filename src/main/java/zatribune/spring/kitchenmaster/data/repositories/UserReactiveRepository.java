package zatribune.spring.kitchenmaster.data.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.data.entities.User;

public interface UserReactiveRepository extends ReactiveMongoRepository<User,String> {
    Mono<User> findDistinctByUsername(String username);
}
