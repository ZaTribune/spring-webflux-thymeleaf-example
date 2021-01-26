package zatribune.spring.cookmaster.data.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zatribune.spring.cookmaster.data.entities.Ingredient;
import zatribune.spring.cookmaster.exceptions.MyNotFoundException;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
//@DataJpaTest
@DataMongoTest
class IngredientRepositoryIT {

    @Autowired
    IngredientRepository repository;

    @BeforeEach
    void setUp() {

    }
    @Test
    void save(){
        Ingredient ingredient=new Ingredient();
        ingredient.setDescription("Dummy");
        ingredient=repository.save(ingredient);
        assertNotNull(ingredient.getId());
    }

    @Test
    void findById() {
        //save one and get its id
        Ingredient ingredient=new Ingredient();
        ingredient.setDescription("Dummy");

        ingredient=repository.save(ingredient);
        Optional<Ingredient> ing= repository.findById(ingredient.getId().toString());

        ing.ifPresent(Assertions::assertNotNull);
        ing.orElseThrow(MyNotFoundException::new);
    }

    @Test
    void findAllByRecipe() {
    }
}