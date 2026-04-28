package com.tribune.demo.km.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.data.entity.User;
import com.tribune.demo.km.data.repository.UserReactiveRepository;

import java.util.Collections;
import java.util.HashSet;

@Service
public record UserServiceImpl(UserReactiveRepository userRepository,
                              PasswordEncoder bCryptPasswordEncoder) implements UserService {

    @Override
    public Mono<User> save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        // Default to ROLE_USER if no roles specified (roles are now stored as strings to avoid DBRef issues)
        if (user.getRoleNames() == null || user.getRoleNames().isEmpty()) {
            user.setRoleNames(new HashSet<>(Collections.singletonList("ROLE_USER")));
        }
        return userRepository.save(user);
    }

    @Override
    public Mono<User> saveToDefaults(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(false);
        user.setAccountNonLocked(false);
        user.setAccountNonExpired(false);
        user.setCredentialsNotExpired(false);
        // Default to ROLE_USER if no roles specified (roles are now stored as strings to avoid DBRef issues)
        if (user.getRoleNames() == null || user.getRoleNames().isEmpty()) {
            user.setRoleNames(new HashSet<>(Collections.singletonList("ROLE_USER")));
        }
        return userRepository.save(user);
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return userRepository.findDistinctByUsername(username);
    }
}