package zatribune.spring.cookmaster.data.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zatribune.spring.cookmaster.data.entities.Ingredient;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@ExtendWith(SpringExtension.class)
@DataJpaTest
class IngredientRepositoryIT {

    @Autowired
    IngredientRepository repository;


    @BeforeEach
    void setUp() {
    }

    @Test
    void findIngredientById() {
        Optional<Ingredient> ing= repository.findById(1L);
        Set<Ingredient>ingredientSet= StreamSupport.stream(repository.findAll().spliterator(),false)
                .collect(Collectors.toSet());
        ingredientSet.forEach(ingredient -> System.out.println(ingredient.getDescription()));

        ing.ifPresent(Assertions::assertNotNull);
    }

    @Test
    void findAllByRecipe() {
    }
}