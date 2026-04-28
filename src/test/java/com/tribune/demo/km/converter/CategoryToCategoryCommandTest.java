package com.tribune.demo.km.converter;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.tribune.demo.km.command.CategoryCommand;
import com.tribune.demo.km.data.entity.Category;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class CategoryToCategoryCommandTest {


    CategoryToCategoryCommand converter;
    ObjectId id;
    String description = "a dummy category description";

    @BeforeEach
    void setUp() {
        converter = new CategoryToCategoryCommand();
        id = new ObjectId();
    }

    @Test
    void testEmptyObject() {
        assertNotNull(converter.convert(new Category()));
    }


    @Test
    void convert() {
        Category source = new Category();
        source.setId(id);
        source.setDescription(description);
        source.setRecipes(new HashSet<>());

        CategoryCommand category = converter.convert(source);

        assertNotNull(category);
        assertEquals(source.getId().toString(), category.getId());
        assertEquals(description, category.getDescription());
    }

}