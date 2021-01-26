package zatribune.spring.cookmaster.data.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zatribune.spring.cookmaster.data.entities.UnitMeasure;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

//replaced @RunWith(SpringRunner.class) of JUnit 4
@ExtendWith(SpringExtension.class)
@DataMongoTest
//to bring the embedded database for testing
class UnitMeasureRepositoryIT {

    @Autowired
    UnitMeasureRepository unitMeasureRepository;
    @Autowired
    RecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DirtiesContext// this will reset the context so that it's initialized entirely again
    void findUnitOfMeasureByDescription() {
        Optional<UnitMeasure>unitOfMeasure= unitMeasureRepository.findUnitOfMeasureByDescription("ounce");
        unitOfMeasure.ifPresent(u->assertEquals("ounce",u.getDescription()));
        assertFalse(unitOfMeasure.isEmpty());
    }
    @Test
    @DirtiesContext// this will reset the context so that it's initialized entirely again
    void findAll() {
        Set<UnitMeasure> unitMeasures= StreamSupport.stream(unitMeasureRepository.findAll().spliterator(),false)
                .collect(Collectors.toSet());
        System.out.println(unitMeasures.size());

    }
}