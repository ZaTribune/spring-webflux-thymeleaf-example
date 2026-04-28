package com.tribune.demo.km.converter;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.tribune.demo.km.command.CategoryCommand;
import com.tribune.demo.km.data.entity.Category;

import static org.junit.jupiter.api.Assertions.*;

class CategoryCommandToCategoryTest {

    CategoryCommandToCategory converter;
    String description = "a dummy category description";

    @BeforeEach
    void setUp() {
        converter = new CategoryCommandToCategory();
    }

    @Test
    void testEmptyObject() {
        assertNotNull(converter.convert(new CategoryCommand()));
    }

    @Test
    void convert() {
        CategoryCommand source = new CategoryCommand();
        source.setId(new ObjectId().toString());
        source.setDescription(description);


        Category category = converter.convert(source);

        assertNotNull(category);
        assertEquals(source.getId(), category.getId().toString());
        assertEquals(description, category.getDescription());
    }
}