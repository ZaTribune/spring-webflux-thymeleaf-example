package zatribune.spring.kitchenmaster.data.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import zatribune.spring.kitchenmaster.data.entities.UnitMeasure;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class UnitMeasureReactiveRepositoryIT {

    @Autowired
    UnitMeasureReactiveRepository repository;
    String description="ounce";

    @BeforeEach
    void setUp() {
        repository.deleteAll().block();
    }

    @Test
    void save(){
        UnitMeasure unitMeasure=new UnitMeasure();
        unitMeasure.setDescription(description);
        repository.save(unitMeasure).block();
        assertEquals(1L,repository.count().block());
    }

    @Test
    void findByDescription() {
        UnitMeasure unitMeasure=new UnitMeasure();
        unitMeasure.setDescription(description);
        repository.save(unitMeasure).block();
        UnitMeasure returnedUnitMeasure= repository.findByDescription(description).block();
        assertNotNull(returnedUnitMeasure);
        assertEquals(description,returnedUnitMeasure.getDescription());
    }

}