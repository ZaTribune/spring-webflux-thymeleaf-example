package zatribune.spring.cookmaster.data.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zatribune.spring.cookmaster.data.entities.UnitMeasure;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

//replaced @RunWith(SpringRunner.class) of JUnit 4
@ExtendWith(SpringExtension.class)
@DataJpaTest//to bring the embedded database for testing
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

    }
}