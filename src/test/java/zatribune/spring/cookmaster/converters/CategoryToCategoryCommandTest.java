package zatribune.spring.cookmaster.converters;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zatribune.spring.cookmaster.commands.CategoryCommand;
import zatribune.spring.cookmaster.data.entities.Category;

import static org.junit.jupiter.api.Assertions.*;

class CategoryToCategoryCommandTest {


    private CategoryToCategoryCommand converter;
    private final String id="0x875454";
    private final String description="a dummy category description";

    @BeforeEach
    void setUp() {
        converter=new CategoryToCategoryCommand();
    }

    @Test
    void testEmptyObject(){
        assertNotNull(converter.convert(new Category()));
    }

    @Test
    void testNullObject(){
        assertNull(converter.convert(null));
    }


    @Test
    void convert() {
        Category source=new Category();
        source.setId(new ObjectId());
        source.setDescription(description);

        CategoryCommand category=converter.convert(source);

        assertNotNull(category);
        assertEquals(source.getId().toString(),category.getId());
        assertEquals(description,category.getDescription());
    }

}