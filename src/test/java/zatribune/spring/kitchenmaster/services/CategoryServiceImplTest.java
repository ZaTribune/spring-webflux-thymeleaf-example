package zatribune.spring.kitchenmaster.services;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.converters.CategoryToCategoryCommand;
import zatribune.spring.kitchenmaster.data.entities.Category;
import zatribune.spring.kitchenmaster.data.repositories.CategoryReactiveRepository;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    CategoryReactiveRepository categoryRepository;
    @Mock
    CategoryToCategoryCommand categoryToCategoryCommand;

    CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService=new CategoryServiceImpl(categoryRepository,categoryToCategoryCommand);
    }

    @Test
    void getAllCategories() {
        Category category1=new Category();
        category1.setDescription("Egyptian");
        Category category2=new Category();
        category2.setDescription("Australian");

        when(categoryService.getAllCategories()).thenReturn(Flux.just(category1,category2));

        List<Category> returnedSet=categoryService.getAllCategories().collectList().block();
        assertNotNull(returnedSet);
        assertEquals(2,returnedSet.size());
        verify(categoryRepository,times(1)).findAll();

    }

    @Test
    void getCategoryById() {
        Category category1=new Category();
        ObjectId id=new ObjectId();
        category1.setId(id);
        category1.setDescription("Russian");

        when(categoryRepository.findById(id.toString())).thenReturn(Mono.just(category1));
        Category returnedCategory=categoryService.getCategoryById(id.toString()).block();

        assertEquals(category1,returnedCategory);
        verify(categoryRepository,times(1)).findById(id.toString());
        verify(categoryRepository,never()).findAll();

    }
}