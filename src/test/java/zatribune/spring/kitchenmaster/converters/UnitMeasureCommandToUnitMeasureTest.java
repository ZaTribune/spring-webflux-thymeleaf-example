package zatribune.spring.kitchenmaster.converters;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zatribune.spring.kitchenmaster.commands.UnitMeasureCommand;
import zatribune.spring.kitchenmaster.data.entities.UnitMeasure;

import static org.junit.jupiter.api.Assertions.*;

class UnitMeasureCommandToUnitMeasureTest {

    String description="a dummy description";
    UnitMeasureCommandToUnitMeasure converter;

    @BeforeEach
    void setUp() {
        converter=new UnitMeasureCommandToUnitMeasure();
    }

    @Test
    void testEmptyObject(){
        assertNotNull(converter.convert(new UnitMeasureCommand()));
    }

    @Test
    void convert() {
        UnitMeasureCommand command=new UnitMeasureCommand();
        command.setId(new ObjectId().toString());
        command.setDescription(description);

        UnitMeasure unitMeasure=converter.convert(command);

        assertNotNull(unitMeasure);
        assertEquals(command.getId(),unitMeasure.getId().toString());
        assertEquals(description,unitMeasure.getDescription());
    }
}