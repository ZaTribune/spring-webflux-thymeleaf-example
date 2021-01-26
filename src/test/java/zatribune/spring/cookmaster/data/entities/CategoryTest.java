package zatribune.spring.cookmaster.data.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    Category category;
    String id;

    @BeforeEach
    void setUp() {
        id="0x456897";
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