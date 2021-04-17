package zatribune.spring.kitchenmaster.services;


import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.data.entities.User;

public interface UserService {
    void save(User user);
    void saveToDefaults(User user);
    Mono<User> findByUsername(String username);
}
