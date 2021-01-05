package zatribune.spring.cookmaster.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zatribune.spring.cookmaster.data.entities.Category;
import zatribune.spring.cookmaster.services.CategoryService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class CategoryControllerTest {

    @Mock
    CategoryService categoryService;
    CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryController=new CategoryController(categoryService);
    }

    @Test
    void getCategoryPage() throws Exception {
        Category category=new Category();
        category.setId(20L);
        category.setDescription("Japanese");


        when(categoryService.getCategoryById(anyLong())).thenReturn(Optional.of(category));

        Optional<Category>returnedCategory=categoryService.getCategoryById(20L);
        returnedCategory.ifPresent(c->assertEquals(20L,c.getId()));


        MockMvc mockMvc= MockMvcBuilders.standaloneSetup(categoryController).build();
        mockMvc.perform(get("/category/show/20"))
                .andExpect(status().isOk())
                .andExpect(view().name("/category/show"));

    }
}