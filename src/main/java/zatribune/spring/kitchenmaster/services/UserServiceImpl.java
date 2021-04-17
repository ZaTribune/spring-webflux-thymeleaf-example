package zatribune.spring.kitchenmaster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.data.entities.Role;
import zatribune.spring.kitchenmaster.data.entities.User;
import zatribune.spring.kitchenmaster.data.repositories.UserReactiveRepository;
import zatribune.spring.kitchenmaster.data.repositories.RoleReactiveRepository;

import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserReactiveRepository userRepository;
    private final RoleReactiveRepository roleReactiveRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserReactiveRepository userRepository, RoleReactiveRepository roleReactiveRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleReactiveRepository = roleReactiveRepository;
        this.bCryptPasswordEncoder = passwordEncoder;
    }

    @Override
    public void save(User user) {
        //todo: check authorities
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role role= roleReactiveRepository.findAll().blockFirst();
        if (role!=null)
            user.setRoles(new HashSet<>(List.of(role)));

        userRepository.save(user);
    }

    @Override
    public void saveToDefaults(User user) {
        //todo: check authorities
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        Role role= roleReactiveRepository.findAll().blockFirst();
        if (role!=null)
            user.setRoles(new HashSet<>(List.of(role)));

        user.setEnabled(false);
        user.setAccountNonLocked(false);
        user.setAccountNonExpired(false);
        user.setCredentialsNotExpired(false);
        userRepository.save(user);
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return userRepository.findDistinctByUsername(username);
    }
}
