package com.tribune.demo.km.data.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.tribune.demo.km.data.entity.UnitMeasure;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@SpringBootTest
class UnitMeasureReactiveRepositoryIT {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    UnitMeasureReactiveRepository repository;
    String description = "ounce";

    @BeforeEach
    void setUp() {
        repository.deleteAll().block();
    }

    @Test
    void save() {
        UnitMeasure unitMeasure = new UnitMeasure();
        unitMeasure.setDescription(description);
        repository.save(unitMeasure).block();
        assertEquals(1L, repository.count().block());
    }

    @Test
    void findByDescription() {
        UnitMeasure unitMeasure = new UnitMeasure();
        unitMeasure.setDescription(description);
        repository.save(unitMeasure).block();
        UnitMeasure returnedUnitMeasure = repository.findByDescription(description).block();
        assertNotNull(returnedUnitMeasure);
        assertEquals(description, returnedUnitMeasure.getDescription());
    }

}