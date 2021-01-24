package zatribune.spring.cookmaster.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zatribune.spring.cookmaster.commands.CategoryCommand;
import zatribune.spring.cookmaster.data.entities.Category;
import zatribune.spring.cookmaster.data.repositories.CategoryRepository;
import zatribune.spring.cookmaster.services.CategoryService;
import zatribune.spring.cookmaster.services.CategoryServiceImpl;
import zatribune.spring.cookmaster.services.ImageService;
import zatribune.spring.cookmaster.services.ImageServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    ImageController controller;

    @Mock
    ImageService imageService;
    @Mock
    CategoryService categoryService;
    @Mock
    CategoryRepository categoryRepository;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        controller = new ImageController(imageService,categoryService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
        imageService=new ImageServiceImpl(categoryRepository);
    }

    @Test
    void handleImagePost() throws Exception {
        // name:-> the parameter name that's specified on the @RequestParameter
        MockMultipartFile multipartFile = new MockMultipartFile("imageFile",
                "texting.txt",
                "text/plain", "zatribune.spring".getBytes());
        mockMvc.perform(multipart("/category/1/uploadImage").file(multipartFile))
                //.andExpect(status().is3xxRedirection()) // if there's a redirection
                //.andExpect(header().string("Location","/category/1/show")) // if displayed on a separate page
                .andExpect(status().isOk());



        Long id=15L;
        Category category=new Category();
        category.setId(id);
        category.setDescription("Atlasian");
        Optional<Category>optionalCategory=Optional.of(category);

        //because it needs an existing category entity
        when(categoryRepository.findById(id)).thenReturn(optionalCategory);
        ArgumentCaptor<Category>captor=ArgumentCaptor.forClass(Category.class);
        //when
        imageService.saveImageFile(id,multipartFile);

        //verify(imageService,times(1)).saveImageFile(anyLong(),any());
        verify(categoryRepository,times(1)).save(captor.capture());

        Category returnedCategory=captor.getValue();
        assertEquals(multipartFile.getBytes().length,returnedCategory.getImage().length);

    }

    @Test
    void showProductImageBadId()throws Exception{
        mockMvc.perform(get("/category/mmm/image"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("errors/400"));
    }

    @Test
    void renderImageFromDB() throws Exception {
        CategoryCommand categoryCommand=new CategoryCommand();
        Long id=15L;
        categoryCommand.setId(id);
        categoryCommand.setDescription("Atlasian");
        String fakeImageTxt="Dummy image txt";
        Byte[]wrapperBytes=new Byte[fakeImageTxt.getBytes().length];
        int i=0;
        for(byte b:fakeImageTxt.getBytes())
            wrapperBytes[i++]=b;

        categoryCommand.setImage(wrapperBytes);

        when(categoryService.getCategoryCommandById(id)).thenReturn(categoryCommand);

        // if shown in a separate page
        MockHttpServletResponse response=mockMvc.perform(get("/category/15/image"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[]responseBytes=response.getContentAsByteArray();
        assertEquals(fakeImageTxt.getBytes().length,responseBytes.length);
    }
}