package zatribune.spring.cookmaster.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zatribune.spring.cookmaster.commands.UnitMeasureCommand;
import zatribune.spring.cookmaster.data.entities.UnitMeasure;

import static org.junit.jupiter.api.Assertions.*;

class UnitMeasureCommandToUnitMeasureTest {

    private final String description="a dummy description";
    private final Long aLong=15L;
    private UnitMeasureCommandToUnitMeasure converter;

    @BeforeEach
    void setUp() {
        converter=new UnitMeasureCommandToUnitMeasure();
    }
    @Test
    void testNullObject(){
        assertNull(converter.convert(null));
    }
    @Test
    void testEmptyObject(){
        assertNotNull(converter.convert(new UnitMeasureCommand()));
    }

    @Test
    void convert() {
        UnitMeasureCommand command=new UnitMeasureCommand();
        command.setId(aLong);
        command.setDescription(description);

        UnitMeasure unitMeasure=converter.convert(command);

        assertNotNull(unitMeasure);
        assertEquals(aLong,unitMeasure.getId());
        assertEquals(description,unitMeasure.getDescription());
    }
}