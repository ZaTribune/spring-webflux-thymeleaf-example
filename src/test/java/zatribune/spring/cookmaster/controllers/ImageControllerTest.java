package zatribune.spring.cookmaster.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zatribune.spring.cookmaster.commands.CategoryCommand;
import zatribune.spring.cookmaster.converters.CategoryToCategoryCommand;
import zatribune.spring.cookmaster.data.entities.Category;
import zatribune.spring.cookmaster.data.repositories.CategoryRepository;
import zatribune.spring.cookmaster.exceptions.MyNotFoundException;
import zatribune.spring.cookmaster.services.CategoryServiceImpl;
import zatribune.spring.cookmaster.services.ImageServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    ImageController controller;

    @InjectMocks
    ImageServiceImpl imageService;//the image service will be injected with the category service
    @InjectMocks
    CategoryServiceImpl categoryService;//the categoryService will be injected with the repository
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    CategoryToCategoryCommand categoryToCategoryCommand;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        controller = new ImageController(imageService,categoryService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
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



        String id="0x875454";
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
        String id="zzzzzzz";
        //when(categoryRepository.findById(id)).thenThrow(new MyNotFoundException());
        mockMvc.perform(get("/category/"+id+"/image"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("errors/404"));
    }

    @Test
    void renderImageFromDB() throws Exception {
        String id="0x875454";
        String description="Atlasian";
        Category category=new Category();
        category.setId(id);
        category.setDescription(description);
        CategoryCommand categoryCommand=new CategoryCommand();
        categoryCommand.setId(id);
        categoryCommand.setDescription(description);
        String fakeImageTxt="Dummy image txt";
        Byte[]wrapperBytes=new Byte[fakeImageTxt.getBytes().length];
        int i=0;
        for(byte b:fakeImageTxt.getBytes())
            wrapperBytes[i++]=b;
        categoryCommand.setImage(wrapperBytes);
        category.setImage(wrapperBytes);
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryService.getCategoryCommandById(id)).thenReturn(categoryCommand);

        System.out.println(""+categoryRepository.findById(id));
        // if shown in a separate page
        MockHttpServletResponse response=mockMvc.perform(get("/category/"+id+"/image"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[]responseBytes=response.getContentAsByteArray();
        assertEquals(fakeImageTxt.getBytes().length,responseBytes.length);
    }
}