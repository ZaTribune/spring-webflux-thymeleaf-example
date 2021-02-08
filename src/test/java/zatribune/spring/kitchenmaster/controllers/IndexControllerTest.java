package zatribune.spring.kitchenmaster.controllers;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@ExtendWith(MockitoExtension.class)
@WebFluxTest(IndexController.class)
class IndexControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
    }
    //webAppContextSetup will bring the Spring context therefore our test will no longer be a unit testing

    @Test
    void getIndexPage() { // this is an example of test-driven-development {given-when-then}
        List<String> result =
                webTestClient.get().uri("/").exchange()
                        .expectStatus().isOk()
                        .returnResult(String.class).getResponseBody()
                        .collectList().block();
        assertNotNull(result);
        assertTrue(result.size()>0);
    }
}