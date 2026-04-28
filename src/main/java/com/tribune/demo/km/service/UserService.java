package com.tribune.demo.km.service;


import reactor.core.publisher.Mono;
import com.tribune.demo.km.data.entity.User;

public interface UserService {
    Mono<User> save(User user);
    Mono<User> saveToDefaults(User user);
    Mono<User> findByUsername(String username);
}
