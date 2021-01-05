package zatribune.spring.cookmaster.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import zatribune.spring.cookmaster.data.entities.Recipe;
import zatribune.spring.cookmaster.services.CategoryService;
import zatribune.spring.cookmaster.services.RecipeService;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    @InjectMocks
    IndexController indexController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testMockMVC() throws Exception{
        //webAppContextSetup will bring the Spring context therefore our test will no longer be a unit testing
        MockMvc mockMvc= MockMvcBuilders
                .standaloneSetup(indexController)
                .build();
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void getIndexPage() { // this is an example of test-driven-development {given-when-then}

    }
}