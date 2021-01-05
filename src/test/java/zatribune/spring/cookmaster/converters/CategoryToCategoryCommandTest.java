package zatribune.spring.cookmaster.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zatribune.spring.cookmaster.commands.CategoryCommand;
import zatribune.spring.cookmaster.data.entities.Category;

import static org.junit.jupiter.api.Assertions.*;

class CategoryToCategoryCommandTest {


    private CategoryToCategoryCommand converter;
    private final Long id=15L;
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
        source.setId(id);
        source.setDescription(description);

        CategoryCommand category=converter.convert(source);

        assertNotNull(category);
        assertEquals(id,category.getId());
        assertEquals(description,category.getDescription());
    }

}