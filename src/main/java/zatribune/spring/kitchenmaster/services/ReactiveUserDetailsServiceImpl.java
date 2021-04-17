package zatribune.spring.kitchenmaster.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.data.entities.User;
import zatribune.spring.kitchenmaster.data.repositories.UserReactiveRepository;

@Slf4j
@Service
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserReactiveRepository userRepository;

    @Autowired
    public ReactiveUserDetailsServiceImpl(UserReactiveRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional(readOnly = true)
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        log.info("find by username "+username);
        Mono<User> user = userRepository.findDistinctByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException(username);
        //Cast the current Mono produced type into a target produced type.
        return user.cast(UserDetails.class);
    }
}
