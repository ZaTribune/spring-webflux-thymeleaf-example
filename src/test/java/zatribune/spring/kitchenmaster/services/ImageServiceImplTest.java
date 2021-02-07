package zatribune.spring.kitchenmaster.services;

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
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.data.entities.Category;
import zatribune.spring.kitchenmaster.data.repositories.CategoryReactiveRepository;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @InjectMocks
    ImageServiceImpl imageService;

    @Mock
    CategoryReactiveRepository categoryRepository;
    @Captor
    ArgumentCaptor<Category>captor;

    ObjectId id;

    @BeforeEach
    void setUp() {
        id=new ObjectId();
    }

    @Test
    void saveImageFile() throws IOException {
        Category category=new Category();
        category.setId(id);
        category.setDescription("Atlasian");

        // name:-> the parameter name that's specified on the @RequestParameter
        MockMultipartFile multipartFile = new MockMultipartFile("imageFile",
                "texting.txt",
                "text/plain", "zatribune.spring".getBytes());
        //because it needs an existing category entity
        when(categoryRepository.findById(id.toString())).thenReturn(Mono.just(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(Mono.just(category));

        //imageService.saveImageFile(id.toString(),multipartFile);

        verify(categoryRepository,times(1)).save(captor.capture());
        verify(categoryRepository,times(1)).findById(anyString());
        Category returnedCategory=captor.getValue();
        assertEquals(multipartFile.getBytes().length,returnedCategory.getImage().length);
    }
}