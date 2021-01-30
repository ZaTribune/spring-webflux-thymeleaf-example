package zatribune.spring.cookmaster.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import zatribune.spring.cookmaster.data.entities.Category;
import zatribune.spring.cookmaster.data.repositories.CategoryReactiveRepository;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final CategoryReactiveRepository categoryRepository;

    @Autowired
    public ImageServiceImpl(CategoryReactiveRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Mono<Void> saveImageFile(String id, MultipartFile file) {
        log.info("received an Image file: " + file.getResource().getFilename());
        //both methods must be on the same level of hierarchy meaning that you just can't call save()
        //inside the findById() stream
        Mono<Category> categoryMono = categoryRepository
                .findById(id)
                .map(category -> {
                    try {
                        //Wrapping the file bytes inside a new Byte[] for the object
                        Byte[] objectBytes = new Byte[file.getBytes().length];
                        int i = 0;
                        for (byte b : file.getBytes())
                            objectBytes[i++] = b;
                        category.setImage(objectBytes);
                        //categoryRepository.save(category).subscribe();
                    } catch (IOException e) {
                        log.error("ERROR", e);
                        e.printStackTrace();
                    }
                    return category;
                });

        return categoryRepository.save(categoryMono.block()).transform(categoryMono1 -> Mono.empty());
    }
}
