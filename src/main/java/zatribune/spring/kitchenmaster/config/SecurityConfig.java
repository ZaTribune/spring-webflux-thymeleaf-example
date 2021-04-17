package zatribune.spring.kitchenmaster.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.data.repositories.UserReactiveRepository;
import zatribune.spring.kitchenmaster.services.ReactiveUserDetailsServiceImpl;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final UserReactiveRepository userReactiveRepository;

    @Autowired
    public SecurityConfig(UserReactiveRepository userReactiveRepository) {
        this.userReactiveRepository = userReactiveRepository;
    }

    //the service
    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService() {
        return new ReactiveUserDetailsServiceImpl(userReactiveRepository);
    }

    //the encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        //return NoOpPasswordEncoder.getInstance();
        //strength the log rounds to use, between 4 and 31
        return new BCryptPasswordEncoder(12);
    }

    //the authentication manager
    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        // I figured it myself with guessing!! haha ^_^
        UserDetailsRepositoryReactiveAuthenticationManager manager =
                new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService());
        //passing the password encoder
        manager.setPasswordEncoder(passwordEncoder());
        return manager;
    }

    /*
        @Bean
        public ReactiveUserDetailsService reactiveUserDetailsServiceImpl(){//the name is important with beans
            // for a predefined users
            //MapReactiveUserDetailsService service=new MapReactiveUserDetailsService(List.of(user1,user2));
        }
    */

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        return http
                .anonymous()
                .and().formLogin().authenticationFailureHandler((exchange, exception) -> {
                    ServerHttpResponse response=exchange.getExchange().getResponse();
                    response.setStatusCode(HttpStatus.BAD_REQUEST);
                    byte[] bytes = exception.getMessage().getBytes(StandardCharsets.UTF_8);
                    DataBuffer buffer = response.bufferFactory().wrap(bytes);
                    return response.writeWith(Mono.just(buffer));
                })
                .and().logout().logoutUrl("/logout").logoutSuccessHandler((exchange, authentication) -> {
                    log.info("Logout Successful.");
                    ServerHttpResponse response=exchange.getExchange().getResponse();
                    response.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
                    //Set the (new) location of a resource,
                    //as specified by the Location header.
                    response.getHeaders().setLocation(URI.create("/?logout=true"));
                    return response.setComplete();
                })
                .and().build();
    }

}