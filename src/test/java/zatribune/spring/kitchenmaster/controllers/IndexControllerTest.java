package zatribune.spring.kitchenmaster.controllers;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
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
        String result =
                webTestClient.get().uri("/").exchange()
                        .expectStatus().isOk()
                        .expectBody(String.class)
                        .returnResult().getResponseBody();
        assertNotNull(result);
        assertTrue(result.length()>0);
    }
    @Test
    void getModal(){
        String typeDelete="delete";
        String typeInfo="info";
        String typeError="error";
        String result1 =
                webTestClient.get().uri("/modal/"+typeDelete)
                        .attribute("title","")
                        .attribute("question","")
                        .attribute("info","")
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody(String.class)
                        .returnResult().getResponseBody();
        String result2 =
                webTestClient.get().uri("/modal/"+typeInfo)
                        .attribute("title","")
                        .attribute("info","")
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody(String.class)
                        .returnResult().getResponseBody();
        String result3 =
                webTestClient.get().uri("/modal/"+typeError)
                        .attribute("title","")
                        .attribute("errors",new ArrayList<ObjectError>())
                        .attribute("info","")
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody(String.class)
                        .returnResult().getResponseBody();
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertTrue(result1.length()>0);
        assertTrue(result2.length()>0);
        assertTrue(result3.length()>0);

    }
}