package com.tribune.demo.km.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Controller
public record IndexController() {


    @RequestMapping({"", "/", "/index"})
    public Mono<String> getIndexPage(Model model, @RequestParam(required = false, defaultValue = "false") boolean logout) {
        model.addAttribute("logout", logout);
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    log.info("Authenticated: {}", securityContext.getAuthentication());
                    return Mono.just("index");
                })
                .defaultIfEmpty("index");
    }

    @RequestMapping("/home")
    public String getHomePage() {
        return "index";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model,
                               @RequestParam(required = false) String error,
                               @RequestParam(required = false) String logout) {
        model.addAttribute("loginError", error != null);
        model.addAttribute("logoutSuccess", logout != null);
        return "login";
    }

    /**
     * API endpoint to check current authentication status.
     * Used by the JavaScript SPA to determine whether to show auth-dependent UI elements.
     */
    @GetMapping(value = "/api/auth/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> getAuthStatus() {
        return ReactiveSecurityContextHolder.getContext()
                .mapNotNull(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(auth -> {
                    var roles = auth.getAuthorities().stream()
                            .map(Object::toString)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(Map.<String, Object>of(
                            "authenticated", true,
                            "username", auth.getName(),
                            "roles", roles
                    ));
                })
                .defaultIfEmpty(ResponseEntity.ok(Map.of(
                        "authenticated", false
                )));
    }


}
