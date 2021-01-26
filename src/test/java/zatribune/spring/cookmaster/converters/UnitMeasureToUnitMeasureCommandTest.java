package zatribune.spring.cookmaster.converters;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zatribune.spring.cookmaster.commands.UnitMeasureCommand;
import zatribune.spring.cookmaster.data.entities.UnitMeasure;

import static org.junit.jupiter.api.Assertions.*;

class UnitMeasureToUnitMeasureCommandTest {
    private final String description="a dummy description";
    private final String aLong="0x454589";
    private UnitMeasureToUnitMeasureCommand converter;

    @BeforeEach
    void setUp() {
        converter=new UnitMeasureToUnitMeasureCommand();
    }
    @Test
    void testNullObject(){
        assertNull(converter.convert(null));
    }
    @Test
    void testEmptyObject(){
        assertNotNull(converter.convert(new UnitMeasure()));
    }

    @Test
    void convert() {
        UnitMeasure unitMeasure=new UnitMeasure();
        unitMeasure.setId(new ObjectId(aLong));
        unitMeasure.setDescription(description);

        UnitMeasureCommand unitMeasureCommand=converter.convert(unitMeasure);

        assertNotNull(unitMeasureCommand);
        assertEquals(aLong,unitMeasureCommand.getId());
        assertEquals(description,unitMeasureCommand.getDescription());
    }
}