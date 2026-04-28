package com.tribune.demo.km.data.repository;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.tribune.demo.km.data.entity.Category;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@Testcontainers
@SpringBootTest
class CategoryReactiveRepositoryIT {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    CategoryReactiveRepository reactiveRepository;
    ObjectId id;
    String description;
    String info;


    @BeforeEach
    void setup() {
        reactiveRepository.deleteAll().block();
        id = new ObjectId();
        description = "Egyptian";
        info = "Watch out for the calories";
    }


    @Test
    void save() {
        Category category = new Category();
        category.setDescription(description);
        category.setInfo(info);
        reactiveRepository.save(category).block();
        Long count = reactiveRepository.count().block();
        assertEquals(1L, count);
        log.info("size : {}", count);
    }

    @Test
    void saveAllAndFindAll() {
        Category category = new Category();
        category.setDescription(description);
        category.setInfo(info);
        Category category2 = new Category();
        category2.setDescription(description);
        category2.setInfo(info);
        Category category3 = new Category();
        category3.setDescription(description);
        category3.setInfo(info);
        // saveAll and block to ensure completion
        reactiveRepository.saveAll(Arrays.asList(category, category2, category3)).collectList().block();
        Long resultSize = reactiveRepository.count().block();
        assertEquals(3, resultSize);
        // findAll
        assertEquals(3L, reactiveRepository.findAll().count().block());
        log.info("size {}", resultSize);
    }

    @Test
    void findByDescription() {
        Category category = new Category();
        category.setDescription(description);
        reactiveRepository.save(category).block();

        Category resultCategory = reactiveRepository.findByDescription(description).block();
        assertNotNull(resultCategory);
        assertEquals(description, resultCategory.getDescription());
    }
}