package zatribune.spring.kitchenmaster.controllers;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import zatribune.spring.kitchenmaster.services.ImageService;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//the @WebFluxTest can also be used to test Spring MVC and Spring WebFlux applications
//without a running server via mock server request and response objects.
class ImageControllerTest {

    @Mock
    ImageService imageService;//will be injected with the category service
    @Mock
    CategoryService categoryService;//will be injected with the repository and the converter
    @Mock
    CategoryReactiveRepository categoryRepository;

    WebTestClient webTestClient;
    ObjectId id;
    String description="Atlassian";

    @BeforeEach
    void setUp() {
        id=new ObjectId();
        webTestClient=WebTestClient.bindToController(new ImageController(imageService,categoryService)).build();
        //This setup connects to a running server to perform full, end-to-end HTTP tests:
        //webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    }

    @Test
    void uploadImage() throws Exception {
        byte[]fileBytes="Muhammad Ali".getBytes();
        Category category=new Category();
        category.setId(id);
        category.setDescription("Atlassian");
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
    }
}