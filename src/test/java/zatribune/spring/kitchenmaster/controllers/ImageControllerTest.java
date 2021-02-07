package zatribune.spring.kitchenmaster.controllers;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.commands.CategoryCommand;
import zatribune.spring.kitchenmaster.data.entities.Category;
import zatribune.spring.kitchenmaster.data.repositories.CategoryReactiveRepository;
import zatribune.spring.kitchenmaster.services.CategoryServiceImpl;
import zatribune.spring.kitchenmaster.services.ImageServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    ImageController controller;

    @InjectMocks
    ImageServiceImpl imageService;//will be injected with the category service
    @Mock
    CategoryServiceImpl categoryService;//will be injected with the repository and the converter
    @Mock
    CategoryReactiveRepository categoryRepository;
    @Captor
    ArgumentCaptor<Category> captor;

    MockMvc mockMvc;
    ObjectId id;
    String description="Atlasian";

    @BeforeEach
    void setUp() {
        controller = new ImageController(imageService,categoryService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
        id=new ObjectId();
    }

    @Test
    void uploadImage() throws Exception {

        Category category=new Category();
        category.setId(id);
        category.setDescription("Atlasian");

        // name:-> the parameter name that's specified on the @RequestParameter
        MockMultipartFile multipartFile = new MockMultipartFile("imageFile",
                "texting.txt",
                "text/plain", "zatribune.spring".getBytes());
        //because it needs an existing category entity
        when(categoryRepository.findById(id.toString())).thenReturn(Mono.just(category));
        when(categoryRepository.save(any())).thenReturn(Mono.just(category));

        mockMvc.perform(multipart("/category/"+id.toString()+"/uploadImage").file(multipartFile))
                //.andExpect(status().is3xxRedirection()) // if there's a redirection
                //.andExpect(header().string("Location","/category/1/show")) // if displayed on a separate page
                .andExpect(status().isOk());

        //verify(imageService,times(1)).saveImageFile(anyLong(),any());
        verify(categoryRepository,times(1)).save(captor.capture());
        verify(categoryRepository,times(1)).findById(anyString());
        Category returnedCategory=captor.getValue();
        assertEquals(multipartFile.getBytes().length,returnedCategory.getImage().length);

    }

    @Test
    void showProductImage() throws Exception {
        CategoryCommand categoryCommand=new CategoryCommand();
        categoryCommand.setId(id.toString());
        categoryCommand.setDescription(description);
        String fakeImageTxt="Dummy image txt";
        Byte[]wrapperBytes=new Byte[fakeImageTxt.getBytes().length];
        int i=0;
        for(byte b:fakeImageTxt.getBytes())
            wrapperBytes[i++]=b;
        categoryCommand.setImage(wrapperBytes);

        when(categoryService.getCategoryCommandById(id.toString())).thenReturn(Mono.just(categoryCommand));
        // if shown in a separate page
//        MockHttpServletResponse response=mockMvc.perform(get("/category/"+id.toString()+"/image"))
//                .andExpect(status().isOk())
//                .andReturn().getResponse();
//
//        byte[]responseBytes=response.getContentAsByteArray();
//        assertEquals(fakeImageTxt.getBytes().length,responseBytes.length);
    }
}