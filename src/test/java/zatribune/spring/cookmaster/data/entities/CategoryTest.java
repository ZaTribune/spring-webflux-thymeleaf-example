package zatribune.spring.cookmaster.data.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    Category category;

    @BeforeEach
    void setUp() {
        category=new Category();
        category.setId(44L);
    }

    @Test
    void getId() {
        Long id=44L;
        assertEquals(id,category.getId());
    }

    @Test
    void getDescription() {
    }

    @Test
    void getRecipes() {
    }
}