package com.tribune.demo.km.service;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.data.repository.UserReactiveRepository;

@Slf4j
@Service
public record ReactiveUserDetailsServiceImpl(
        UserReactiveRepository userRepository) implements ReactiveUserDetailsService {


    @Override
    public @NonNull Mono<UserDetails> findByUsername(@NonNull String username) {
        log.info("find by username: {}", username);
        return userRepository.findDistinctByUsername(username)
                .cast(UserDetails.class)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException(username)));
    }
}
