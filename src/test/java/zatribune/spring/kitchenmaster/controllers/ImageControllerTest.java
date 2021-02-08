package zatribune.spring.kitchenmaster.controllers;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.commands.CategoryCommand;
import zatribune.spring.kitchenmaster.data.entities.Category;
import zatribune.spring.kitchenmaster.data.repositories.CategoryReactiveRepository;
import zatribune.spring.kitchenmaster.services.CategoryService;
import zatribune.spring.kitchenmaster.services.ImageServiceImpl;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WebFluxTest(ImageController.class)
class ImageControllerTest {

    @MockBean
    ImageServiceImpl imageService;//will be injected with the category service
    @MockBean
    CategoryService categoryService;//will be injected with the repository and the converter
    @MockBean
    CategoryReactiveRepository categoryRepository;

    @Autowired
    WebTestClient webTestClient;
    ObjectId id;
    String description="Atlassian";

    @BeforeEach
    void setUp() {
        id=new ObjectId();
    }

    @Test
    void uploadImage() throws Exception {
        byte[]fileBytes="Muhammad Ali".getBytes();
        Category category=new Category();
        category.setId(id);
        category.setDescription("Atlasian");
        // name:-> the parameter name that's specified on the @RequestParameter
        MockMultipartFile multipartFile = new MockMultipartFile("imageFile",
                "texting.txt",
                "text/plain", fileBytes);
        //because it needs an existing category entity
        when(categoryRepository.findById(id.toString())).thenReturn(Mono.just(category));

        Byte[]objectBytes=new Byte[fileBytes.length];
        int x=0;
        for (byte b:fileBytes)
            objectBytes[x++]=b;
        category.setImage(objectBytes);

        when(imageService.saveImageFile(anyString(),any())).thenReturn(Mono.just(category));

        MultiValueMap<String,Object>multiValueMap=new LinkedMultiValueMap<>();
        multiValueMap.add("imageFile",multipartFile.getResource());
        Boolean result=webTestClient.post()
                .uri("/category/"+id.toString()+"/uploadImage")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multiValueMap))
                .exchange().expectStatus().isOk().expectBody(Boolean.class).returnResult().getResponseBody();
        assertNotNull(result);
        System.out.println("result: "+result);
        Category returnedCategory=categoryRepository.findById(id.toString()).block();
        assertNotNull(returnedCategory);
        assertEquals(multipartFile.getBytes().length,returnedCategory.getImage().length);

    }

    @Test
    void showProductImage() {
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

        byte[] bytes=webTestClient.get().uri("/category/"+id.toString()+"/image/"+ Instant.now())
                .exchange().expectStatus().isOk()
                .expectBody(byte[].class)
                .returnResult().getResponseBody();

        assertNotNull(bytes);
        assertEquals(fakeImageTxt.getBytes().length,bytes.length);

        // if shown in a separate page
//        MockHttpServletResponse response=mockMvc.perform(get("/category/"+id.toString()+"/image"))
//                .andExpect(status().isOk())
//                .andReturn().getResponse();
//
//        byte[]responseBytes=response.getContentAsByteArray();
//        assertEquals(fakeImageTxt.getBytes().length,responseBytes.length);
    }
}