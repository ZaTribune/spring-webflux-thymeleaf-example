package zatribune.spring.cookmaster.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zatribune.spring.cookmaster.services.ImageService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    ImageController controller;

    @Mock
    ImageService imageService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        controller = new ImageController(imageService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void handleImagePost() throws Exception {
        // name:-> the parameter name that's specified on the @RequestParameter
        MockMultipartFile file = new MockMultipartFile("imageFile",
                "texting.txt",
                "text/plain", "zatribune.spring".getBytes());
        mockMvc.perform(multipart("/category/1/uploadImage").file(file))
                //.andExpect(status().is3xxRedirection()) // if there's a redirection
                //.andExpect(header().string("Location","/category/1/show")) // if displayed on a separate page
                .andExpect(status().isOk());

        verify(imageService,times(1)).saveImageFile(anyLong(),any());
    }
}