package com.tribune.demo.km.controller;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.command.CategoryCommand;
import com.tribune.demo.km.converter.CategoryToCategoryCommand;
import com.tribune.demo.km.data.entity.Category;
import com.tribune.demo.km.service.CategoryService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@WebFluxTest(CategoriesController.class)
/*Using this annotation WebFluxTest(CategoriesController.class) will disable full auto-configuration
 and instead apply only configuration relevant to WebFlux tests
 (i.e. @Controller, @ControllerAdvice, @JsonComponent,Converter/GenericConverter,
 and WebFluxConfigurer beans but not @Component, @Service or @Repository beans).
 Typically, @WebFluxTest is used in combination with @MockBean or @Import
 to create any collaborators required by your @Controller beans.
 if your controller method returns a view/means that you're making a get() request with WebClientTest
 , in that case view will try to get resolved which will not happen until auto configuration feature works.
 If you do not want full application loading, you'll have to mock the controller and its methods<-bad practice.
 If you are looking to load your full application configuration and use WebTestClient,
 you should consider @SpringBootTest combined with @AutoConfigureWebTestClient rather than this annotation.
 */
@WithMockUser(username = "asd", password = "pass")
//annotation used in unit testing and not intended to execute authentication.
// It creates a user which is authenticated already. By default his credentials are user : password
class CategoriesControllerTest {
    @MockitoBean
    CategoryService categoryService;
    @MockitoBean
    CategoryToCategoryCommand categoryToCategoryCommand;
    // a non-blocking, reactive client for testing web servers.
    @Autowired
    WebTestClient webTestClient;

    Category c1, c2, c3;
    CategoryCommand cm1, cm2, cm3;
    ObjectId id1, id2, id3;
    String desc1 = "desc1";
    String desc2 = "desc2";
    String desc3 = "desc3";


    @BeforeEach
    void setUp() {
        id1 = new ObjectId();
        id2 = new ObjectId();
        id3 = new ObjectId();
        c1 = new Category();
        c1.setId(id1);
        c1.setDescription(desc1);
        c2 = new Category();
        c2.setId(id2);
        c2.setDescription(desc2);
        c3 = new Category();
        c3.setId(id3);
        c3.setDescription(desc3);

        cm1 = new CategoryCommand();
        cm1.setId(id1.toString());
        cm1.setDescription(desc1);
        cm2 = new CategoryCommand();
        cm2.setId(id2.toString());
        cm2.setDescription(desc2);
        cm3 = new CategoryCommand();
        cm3.setId(id3.toString());
        cm3.setDescription(desc3);
    }

    @Test
    void getCategoriesHomePage() {

        when(categoryService.getAllCategories()).thenReturn(Flux.just(c1, c2));

        String response = webTestClient.get().uri("/categories")
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody().blockFirst();
        assertNotNull(response);
        assertFalse(response.isEmpty());
    }

    @Test
    void listCategories() {
        Flux<Category> categoryFlux = Flux.just(c1, c2, c3);

        when(categoryService.getAllCategories())
                .thenReturn(categoryFlux);
        when(categoryToCategoryCommand.convert(c1)).thenReturn(cm1);
        when(categoryToCategoryCommand.convert(c2)).thenReturn(cm2);
        when(categoryToCategoryCommand.convert(c3)).thenReturn(cm3);

        List<String> response = webTestClient.get().uri("/api/categories")
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody().collectList().block();
        assertNotNull(response);
        System.out.println(String.join("", response));
        assertTrue(String.join("", response).contains("desc3"));
        assertFalse(response.isEmpty());
    }

    @Test
    void showCategory() {
        when(categoryService.getCategoryById(c1.getId().toString())).thenReturn(Mono.just(c1));

        String response = webTestClient.get().uri("/categories/" + c1.getId()).exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody().blockFirst();
        assertNotNull(response);
        assertFalse(response.isEmpty());
    }


    @Test
    void getCategoryViaApi_ReturnsValidJson() {
        c1.setInfo("Authentic Italian cuisine");
        when(categoryService.getCategoryById(c1.getId().toString())).thenReturn(Mono.just(c1));

        webTestClient.get()
                .uri("/api/categories/" + c1.getId().toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Category.class)
                .consumeWith(result -> {
                    assertNotNull(result.getResponseBody());
                    Category category = result.getResponseBody();
                    assertNotNull(category.getId(), "Category must have ID");
                    assertEquals("desc1", category.getDescription());
                    assertNotNull(category.getInfo(), "Category must have info for card display");
                });
    }


    @Test
    void getAllCategoriesViaApi_ReturnsCompleteList() {
        c1.setInfo("Category 1 info");
        c2.setInfo("Category 2 info");
        c3.setInfo("Category 3 info");

        when(categoryService.getAllCategories()).thenReturn(Flux.just(c1, c2, c3));

        webTestClient.get()
                .uri("/api/categories")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Category.class)
                .consumeWith(result -> {
                    assertNotNull(result.getResponseBody());
                    assertEquals(3, result.getResponseBody().size());
                    result.getResponseBody().forEach(cat -> {
                        assertNotNull(cat.getId());
                        assertNotNull(cat.getDescription());
                        assertNotNull(cat.getInfo());
                    });
                });
    }


    @Test
    void categoryWithLongDescription_LoadsSuccessfully() {
        c1.setInfo("This is a very long description that contains many characters " +
                "and should be properly handled by the card layout to ensure " +
                "that all cards maintain uniform height regardless of content length.");

        when(categoryService.getCategoryById(c1.getId().toString())).thenReturn(Mono.just(c1));

        String response = webTestClient.get()
                .uri("/api/categories/" + c1.getId().toString())
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody().blockFirst();

        assertNotNull(response);
        assertTrue(response.contains("desc1"));
    }
}

