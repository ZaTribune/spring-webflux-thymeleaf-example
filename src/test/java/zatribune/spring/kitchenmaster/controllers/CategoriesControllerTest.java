package zatribune.spring.kitchenmaster.controllers;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.commands.CategoryCommand;
import zatribune.spring.kitchenmaster.converters.CategoryToCategoryCommand;
import zatribune.spring.kitchenmaster.data.entities.Category;
import zatribune.spring.kitchenmaster.services.CategoryService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@WebFluxTest(CategoriesController.class)
/*Using this annotation will disable full auto-configuration and instead apply only configuration
 relevant to WebFlux tests (i.e. @Controller, @ControllerAdvice, @JsonComponent,
 Converter/GenericConverter, and WebFluxConfigurer beans but not
 @Component, @Service or @Repository beans).
 Typically @WebFluxTest is used in combination with
 @MockBean or @Import to create any collaborators required by your @Controller beans.*/
class CategoriesControllerTest {
    @MockBean
    CategoryService categoryService;
    @MockBean
    CategoryToCategoryCommand categoryToCategoryCommand;
    // a non-blocking, reactive client for testing web servers.
    @Autowired
    WebTestClient webTestClient;

    Category c1, c2, c3;
    CategoryCommand cm1,cm2,cm3;
    ObjectId id1,id2,id3;
    String desc1="desc1";
    String desc2="desc2";
    String desc3="desc3";


    @BeforeEach
    void setUp() {
        id1=new ObjectId();
        id2=new ObjectId();
        id3=new ObjectId();
        c1 = new Category();
        c1.setId(id1);
        c1.setDescription(desc1);
        c2 = new Category();
        c2.setId(id2);
        c2.setDescription(desc2);
        c3 = new Category();
        c3.setId(id3);
        c3.setDescription(desc3);

        cm1=new CategoryCommand();
        cm1.setId(id1.toString());
        cm1.setDescription(desc1);
        cm2=new CategoryCommand();
        cm2.setId(id2.toString());
        cm2.setDescription(desc2);
        cm3=new CategoryCommand();
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
        assertTrue(response.length() > 0);
    }

    @Test
    void listCategories() {
        Flux<Category>categoryFlux=Flux.just(c1,c2,c3);

        when(categoryService.getAllCategories())
                .thenReturn(categoryFlux);
        when(categoryToCategoryCommand.convert(c1)).thenReturn(cm1);
        when(categoryToCategoryCommand.convert(c2)).thenReturn(cm2);
        when(categoryToCategoryCommand.convert(c3)).thenReturn(cm3);

        List<String> response = webTestClient.get().uri("/listCategories" + "?s=d")//passing a RequestParam
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody().collectList().block();
        assertNotNull(response);
        System.out.println(String.join("",response));
        assertTrue(String.join("",response).contains("desc3"));
        assertTrue(response.size() > 0);
    }

    @Test
    void showCategory() {
        when(categoryService.getCategoryById(c1.getId().toString())).thenReturn(Mono.just(c1));

        String response = webTestClient.get().uri("/showCategory/" + c1.getId()).exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody().blockFirst();
        assertNotNull(response);
        assertTrue(response.length() > 0);
    }
}