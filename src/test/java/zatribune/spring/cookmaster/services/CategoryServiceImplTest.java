package zatribune.spring.cookmaster.services;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import zatribune.spring.cookmaster.converters.CategoryToCategoryCommand;
import zatribune.spring.cookmaster.data.entities.Category;
import zatribune.spring.cookmaster.data.repositories.CategoryRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @Mock
    CategoryRepository categoryRepository;
    @Mock
    CategoryToCategoryCommand categoryToCategoryCommand;

    CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);//don't forget
        categoryService=new CategoryServiceImpl(categoryRepository,categoryToCategoryCommand);
    }

    @Test
    void getAllCategories() {
        Category category1=new Category();
        category1.setDescription("Egyptian");
        Category category2=new Category();
        category2.setDescription("Australian");
        Set<Category>categories=new HashSet<>();
        categories.add(category1);
        categories.add(category2);

        when(categoryService.getAllCategories()).thenReturn(categories);

        Set<Category>returnedSet=categoryService.getAllCategories();
        assertEquals(2,returnedSet.size());
        verify(categoryRepository,times(1)).findAll();

    }

    @Test
    void getCategoryById() {
        Category category1=new Category();
        String id="0x456456";
        category1.setId(new ObjectId(id));
        category1.setDescription("Russian");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category1));
        Category returnedCategory=categoryService.getCategoryById(id);

        assertEquals(category1,returnedCategory);
        verify(categoryRepository,times(1)).findById(id);
        verify(categoryRepository,never()).findAll();

    }
}