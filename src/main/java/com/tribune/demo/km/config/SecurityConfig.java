package com.tribune.demo.km.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import com.tribune.demo.km.data.repository.UserReactiveRepository;
import com.tribune.demo.km.service.ReactiveUserDetailsServiceImpl;

import java.net.URI;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final UserReactiveRepository userReactiveRepository;

    @Autowired
    public SecurityConfig(UserReactiveRepository userReactiveRepository) {
        this.userReactiveRepository = userReactiveRepository;
    }

    /**
     * Reactive user details service bean
     */
    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService() {
        return new ReactiveUserDetailsServiceImpl(userReactiveRepository);
    }

    /**
     * Password encoder bean - BCrypt with strength 12 (log rounds between 4-31)
     * Higher strength = more secure but slower
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Reactive authentication manager bean for WebFlux
     * Uses ReactiveUserDetailsService with password encoder
     */
    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager manager =
                new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService());
        manager.setPasswordEncoder(passwordEncoder());
        return manager;
    }


    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorize -> authorize
                        // Public pages - no auth required
                        .pathMatchers("/login", "/", "/index", "/home").permitAll()
                        // Public auth status API
                        .pathMatchers("/api/auth/status").permitAll()
                        // Public read-only pages
                        .pathMatchers("/recipes", "/showRecipe/**", "/searchRecipes").permitAll()
                        .pathMatchers("/categories", "/showCategory/**", "/listCategories").permitAll()
                        // Public API endpoints - all GET requests to recipes and categories
                        .pathMatchers("/api/recipes", "/api/recipes/search", "/api/recipes/featured").permitAll()
                        .pathMatchers(org.springframework.http.HttpMethod.GET, "/api/recipes/**").permitAll()
                        .pathMatchers("/api/categories").permitAll()
                        .pathMatchers(org.springframework.http.HttpMethod.GET, "/api/categories/**").permitAll()
                        .pathMatchers(org.springframework.http.HttpMethod.GET, "/api/unit-measures").permitAll()
                        // Protected API write operations
                        .pathMatchers(org.springframework.http.HttpMethod.POST, "/api/recipes").authenticated()
                        .pathMatchers(org.springframework.http.HttpMethod.DELETE, "/api/recipes/**").authenticated()
                        // Static resources
                        .pathMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        .pathMatchers("/webjars/**").permitAll()
                        // Protected recipe write operations - require authentication
                        .pathMatchers("/recipes/new", "/recipes/*/edit").permitAll()
                        .pathMatchers(org.springframework.http.HttpMethod.POST, "/recipes").authenticated()
                        .pathMatchers(org.springframework.http.HttpMethod.DELETE, "/recipes/**").authenticated()
                        // Admin-only category operations - require admin role
                        .pathMatchers("/categories/new", "/categories/*/edit").hasRole("ADMIN")
                        .pathMatchers(org.springframework.http.HttpMethod.POST, "/categories").hasRole("ADMIN")
                        .pathMatchers(org.springframework.http.HttpMethod.DELETE, "/categories/**").hasRole("ADMIN")
                        // Admin-only image uploads
                        .pathMatchers(org.springframework.http.HttpMethod.POST, "/categories/*/image").hasRole("ADMIN")
                        // Anything else requires authentication
                        .anyExchange().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .authenticationFailureHandler((exchange, exception) -> {
                            ServerHttpResponse response = exchange.getExchange().getResponse();
                            response.setStatusCode(HttpStatus.FOUND);
                            response.getHeaders().setLocation(URI.create("/login?error=true"));
                            return response.setComplete();
                        })
                        .authenticationSuccessHandler((exchange, authentication) -> {
                            log.info("Login Successful for user: {}", authentication.getName());
                            ServerHttpResponse response = exchange.getExchange().getResponse();
                            response.setStatusCode(HttpStatus.FOUND);
                            response.getHeaders().setLocation(URI.create("/"));
                            return response.setComplete();
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((exchange, authentication) -> {
                            log.info("Logout Successful.");
                            ServerHttpResponse response = exchange.getExchange().getResponse();
                            response.setStatusCode(HttpStatus.FOUND);
                            response.getHeaders().setLocation(URI.create("/"));
                            return response.setComplete();
                        })
                )
                .build();
    }

}