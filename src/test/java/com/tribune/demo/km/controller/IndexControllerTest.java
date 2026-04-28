package com.tribune.demo.km.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@WebFluxTest(IndexController.class)
@WithMockUser
class IndexControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
    }
    //webAppContextSetup will bring the Spring context therefore our test will no longer be a unit testing

    @Test
    void getIndexPage() { // this is an example of test-driven-development {given-when-then}
        String result =
                webTestClient.get().uri("/").exchange()
                        .expectStatus().isOk()
                        .expectBody(String.class)
                        .returnResult().getResponseBody();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}