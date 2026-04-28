package com.tribune.demo.km.controller;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.data.entity.Category;
import com.tribune.demo.km.service.CategoryService;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@Slf4j
@ExtendWith(MockitoExtension.class)
@WebFluxTest(CategoriesController.class)
class CategoriesApiControllerTest {

    @MockitoBean
    CategoryService categoryService;


    @Autowired
    WebTestClient webTestClient;

    Category category1, category2, category3;
    ObjectId id1, id2, id3;

    @BeforeEach
    void setUp() {
        id1 = new ObjectId();
        id2 = new ObjectId();
        id3 = new ObjectId();

        category1 = new Category();
        category1.setId(id1);
        category1.setDescription("Italian");
        category1.setInfo("Authentic Italian cuisine from different regions");

        category2 = new Category();
        category2.setId(id2);
        category2.setDescription("Mexican");
        category2.setInfo("Traditional Mexican recipes with bold flavors");

        category3 = new Category();
        category3.setId(id3);
        category3.setDescription("Asian");
        category3.setInfo("Diverse Asian cuisines");
    }

    @Test
    void getAllCategoriesApi_ReturnsValidJson() {
        when(categoryService.getAllCategories())
                .thenReturn(Flux.just(category1, category2, category3));

        webTestClient.get()
                .uri("/api/categories")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Category.class)
                .consumeWith(result -> {
                    assertNotNull(result.getResponseBody());
                    assertEquals(3, result.getResponseBody().size());

                    Category cat1 = result.getResponseBody().getFirst();
                    assertNotNull(cat1.getId());
                    assertEquals("Italian", cat1.getDescription());
                    assertNotNull(cat1.getInfo());
                });
    }


    @Test
    void getCategoryByIdApi_ReturnsValidJson() {
        when(categoryService.getCategoryById(id1.toString()))
                .thenReturn(Mono.just(category1));

        webTestClient.get()
                .uri("/api/categories/" + id1.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Category.class)
                .consumeWith(result -> {
                    assertNotNull(result.getResponseBody());
                    Category category = result.getResponseBody();
                    assertEquals(id1, category.getId());
                    assertEquals("Italian", category.getDescription());
                    assertEquals("Authentic Italian cuisine from different regions", category.getInfo());
                });
    }


    @Test
    void getCategoryByIdApi_NotFound_Returns404() {
        String nonExistentId = new ObjectId().toString();
        when(categoryService.getCategoryById(nonExistentId))
                .thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/categories/" + nonExistentId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    void categoryData_ContainsRequiredFieldsForCardRendering() {
        when(categoryService.getAllCategories())
                .thenReturn(Flux.just(category1));

        webTestClient.get()
                .uri("/api/categories")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Category.class)
                .consumeWith(result -> {
                    assertNotNull(result.getResponseBody());
                    assertFalse(result.getResponseBody().isEmpty());
                    Category category = result.getResponseBody().getFirst();

                    // Required fields for category card rendering
                    assertNotNull(category.getId(), "Category must have an ID");
                    assertNotNull(category.getDescription(), "Category must have a description");
                    assertNotNull(category.getInfo(), "Category must have info/details");
                });
    }
}

