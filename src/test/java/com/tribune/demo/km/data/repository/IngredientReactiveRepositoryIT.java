package com.tribune.demo.km.data.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.tribune.demo.km.data.entity.Ingredient;
import com.tribune.demo.km.data.entity.Recipe;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest
class IngredientReactiveRepositoryIT {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    IngredientReactiveRepository ingredientRepository;

    String recipeTitle = "Hamburger";
    String ing1description = "fresh tomatoes";
    String ing2description = "juicy lemon";
    String ing3description = "salt";

    @BeforeEach
    void setUp() {
        ingredientRepository.deleteAll().block();
    }

    @Test
    void save() {
        Ingredient ingredient = new Ingredient();
        ingredient.setDescription(ing1description);
        ingredientRepository.save(ingredient).block();
        assertEquals(1L, ingredientRepository.count().block());
    }

    @Test
    void findByDescription() {
        Ingredient ingredient = new Ingredient();
        ingredient.setDescription(ing1description);
        ingredientRepository.save(ingredient).block();
        Ingredient returnedIngredient = ingredientRepository.findByDescription(ing1description).block();
        assertNotNull(returnedIngredient);
        assertEquals(ing1description, returnedIngredient.getDescription());
    }

    @Test
    void saveAllAndFindAll() {
        Recipe recipe = new Recipe();
        recipe.setTitle(recipeTitle);
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setDescription(ing1description);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setDescription(ing2description);
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setDescription(ing3description);
        /*
        Note that the mapping framework doesn't handle cascading operations.
         So – for instance – if we trigger a save on a parent, the child won't be saved automatically
          – we'll need to explicitly trigger the save on the child if we want to save it as well.
          */
        // Block and collect to ensure all ingredients are saved before counting
        ingredientRepository.saveAll(Arrays.asList(ingredient1, ingredient2, ingredient3))
                .collectList().block();
        assertEquals(3L, ingredientRepository.count().block());
    }
}