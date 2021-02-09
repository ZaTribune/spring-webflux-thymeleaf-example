package zatribune.spring.kitchenmaster.converters;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zatribune.spring.kitchenmaster.commands.CategoryCommand;
import zatribune.spring.kitchenmaster.data.entities.Category;
import zatribune.spring.kitchenmaster.data.entities.Recipe;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class CategoryToCategoryCommandTest {


    CategoryToCategoryCommand converter;
    ObjectId id;
    String description="a dummy category description";

    @BeforeEach
    void setUp() {
        converter=new CategoryToCategoryCommand();
        id=new ObjectId();
    }

    @Test
    void testEmptyObject(){
        assertNotNull(converter.convert(new Category()));
    }



    @Test
    void convert() {
        Category source=new Category();
        source.setId(id);
        source.setDescription(description);
        source.setRecipes(new HashSet<>());

        CategoryCommand category=converter.convert(source);

        assertNotNull(category);
        assertEquals(source.getId().toString(),category.getId());
        assertEquals(description,category.getDescription());
    }

}