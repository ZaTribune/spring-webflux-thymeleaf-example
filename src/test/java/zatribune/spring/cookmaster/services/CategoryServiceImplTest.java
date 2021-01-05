package zatribune.spring.cookmaster.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);//don't forget
        categoryService=new CategoryServiceImpl(categoryRepository);
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
        Long id=15L;
        category1.setId(id);
        category1.setDescription("Russian");
        Optional<Category>optionalCategory=Optional.of(category1);

        when(categoryService.getCategoryById(id)).thenReturn(optionalCategory);
        Optional<Category>returnedCategory=categoryService.getCategoryById(id);

        returnedCategory.ifPresent(c->assertEquals(category1,c));
        verify(categoryRepository,times(1)).findById(id);
        verify(categoryRepository,never()).findAll();

    }
}