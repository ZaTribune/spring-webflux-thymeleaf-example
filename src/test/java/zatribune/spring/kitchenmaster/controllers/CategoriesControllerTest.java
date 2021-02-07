package zatribune.spring.kitchenmaster.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zatribune.spring.kitchenmaster.converters.CategoryToCategoryCommand;
import zatribune.spring.kitchenmaster.services.CategoryService;
import zatribune.spring.kitchenmaster.services.ImageService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@ExtendWith(MockitoExtension.class)
class CategoriesControllerTest {

    @Mock
    private CategoryService categoryService;
    @Mock
    private CategoryToCategoryCommand categoryToCategoryCommand;

    private CategoriesController controller;

    @Mock
    private ImageService imageService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        controller = new CategoriesController(categoryService, categoryToCategoryCommand);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getCategoriesHomePage() {
    }

    @Test
    void listCategories() {
    }

    @Test
    void showCategory() {
    }
}