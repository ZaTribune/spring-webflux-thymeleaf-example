package zatribune.spring.kitchenmaster.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.controllers.ImageController;
import zatribune.spring.kitchenmaster.data.entities.Category;
import zatribune.spring.kitchenmaster.data.repositories.CategoryReactiveRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
/* if you get this error, it's probably something with classes passed to @WebFluxTest
* java.lang.AssertionError: Status expected:<200 OK> but was:<404 NOT_FOUND>
Expected :200 OK
Actual   :404 NOT_FOUND
* */
@ExtendWith(SpringExtension.class)
@WebFluxTest({ImageController.class})
class ImageServiceImplTest {

    @MockBean
    ImageService imageService;
    @MockBean
    CategoryService categoryService;
    @MockBean
    CategoryReactiveRepository categoryRepository;

    @Autowired
    WebTestClient webTestClient;

    ObjectId id;

    @BeforeEach
    void setUp() {
        id = new ObjectId();
    }

    @Test
    void saveImageFile() {
        byte[] fileBytes = "Muhammad ALi".getBytes();
        Category category = new Category();
        category.setId(id);
        category.setDescription("Atlassian");

        when(categoryService.getCategoryById(id.toString())).thenReturn(Mono.just(category));
        Byte[] objectBytes = new Byte[fileBytes.length];
        int i = 0;
        for (byte b : fileBytes)
            objectBytes[i++] = b;

        category.setImage(objectBytes);
        when(imageService.saveImageFile(anyString(), any())).thenReturn(Mono.just(category));

        // name:-> the parameter name that's specified on the @RequestParameter
        MockMultipartFile multipartFile = new MockMultipartFile("imageFile",
                "texting.txt",
                "text/plain", fileBytes);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        //Return a Resource representation of this MultipartFile.
        // This can be used as input to the RestTemplate or the WebClient
        // to expose content length and the filename along with the InputStream.
        System.out.println(multipartFile.getResource());
        body.add("imageFile", multipartFile.getResource());
        Boolean result = webTestClient.post().uri("/category/" + id.toString() + "/uploadImage")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .returnResult().getResponseBody();
        assertNotNull(result);
        Category returnedCategory = categoryService.getCategoryById(id.toString()).block();
        assertNotNull(returnedCategory);
        assertEquals(returnedCategory.getImage().length, fileBytes.length);
        System.out.println(result);
    }
}