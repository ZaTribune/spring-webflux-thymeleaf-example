package zatribune.spring.kitchenmaster.data.entities;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    Category category;
    ObjectId id;

    @BeforeEach
    void setUp() {
        id=new ObjectId();
        category=new Category();
        category.setId(id);
    }

    @Test
    void getId() {
        assertEquals(id,category.getId());
    }

    @Test
    void getDescription() {
    }

    @Test
    void getRecipes() {
    }
}