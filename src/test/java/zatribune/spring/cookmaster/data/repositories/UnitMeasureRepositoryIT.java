package zatribune.spring.cookmaster.data.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zatribune.spring.cookmaster.data.bootstrap.DevBootstrap;
import zatribune.spring.cookmaster.data.entities.UnitMeasure;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest//to bring the embedded database for testing
@ExtendWith(SpringExtension.class)//replaced @RunWith(SpringRunner.class) of JUnit 4
class UnitMeasureRepositoryIT {

    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UnitMeasureRepository unitMeasureRepository;


    @BeforeEach
    void setUp() {
        // this practice is much faster than using @DirtiesContext which will reset the context so that it's initialized entirely again
        recipeRepository.deleteAll();
        categoryRepository.deleteAll();
        unitMeasureRepository.deleteAll();
        DevBootstrap devBootstrap=new DevBootstrap(recipeRepository,categoryRepository,unitMeasureRepository);
        devBootstrap.onApplicationEvent(null);
    }

    @Test
    void findUnitOfMeasureByDescription() {
        Optional<UnitMeasure>unitOfMeasure= unitMeasureRepository.findUnitOfMeasureByDescription("ounce");
        unitOfMeasure.ifPresent(u->assertEquals("ounce",u.getDescription()));
        assertFalse(unitOfMeasure.isEmpty());
    }
    @Test
    void findAll() {
        Set<UnitMeasure> unitMeasures= StreamSupport.stream(unitMeasureRepository.findAll().spliterator(),false)
                .collect(Collectors.toSet());
        assertTrue(unitMeasures.size()>0);
    }

}