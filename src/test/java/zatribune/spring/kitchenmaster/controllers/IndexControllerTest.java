package zatribune.spring.kitchenmaster.controllers;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.validation.ObjectError;
import zatribune.spring.kitchenmaster.data.entities.User;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertTrue(result.length() > 0);
    }

    @Test
    void getModal() {
        for (ModalType type : ModalType.values()) {
            String result = null;
            switch (type) {
                case INFO:
                    result = webTestClient.get().uri("/modal/" + ModalType.INFO)
                            .attribute("title", "")
                            .attribute("question", "")
                            .attribute("info", "")
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody(String.class)
                            .returnResult().getResponseBody();
                    break;
                case DELETE:
                    result = webTestClient.get().uri("/modal/" + ModalType.DELETE)
                            .attribute("title", "")
                            .attribute("info", "")
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody(String.class)
                            .returnResult().getResponseBody();
                    break;
                case ERROR:
                    result = webTestClient.get().uri("/modal/" + ModalType.ERROR)
                            .attribute("title", "")
                            .attribute("errors", new ArrayList<ObjectError>())
                            .attribute("info", "")
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody(String.class)
                            .returnResult().getResponseBody();
                    break;
                case LOGIN:
                    result = webTestClient.get().uri("/modal/" + ModalType.LOGIN)
                            .attribute("user", new User())
                            .attribute("title", "Login")
                            .attribute("info", "Please, enter your credentials.")
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody(String.class)
                            .returnResult().getResponseBody();
            }
            assertNotNull(result);
            assertTrue(result.length() > 0);
        }

    }
}