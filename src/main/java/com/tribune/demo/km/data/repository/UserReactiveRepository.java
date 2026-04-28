package com.tribune.demo.km.data.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.data.entity.User;

public interface UserReactiveRepository extends ReactiveMongoRepository<User,String> {
    Mono<User> findDistinctByUsername(String username);
}
