package zatribune.spring.cookmaster.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zatribune.spring.cookmaster.data.entities.Category;
import zatribune.spring.cookmaster.data.repositories.CategoryRepository;
import zatribune.spring.cookmaster.exceptions.MyNotFoundException;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public ImageServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void saveImageFile(String id, MultipartFile file) {
            log.info("received an Image file: " + file.getResource().getFilename());
            Optional<Category> optionalCategory= categoryRepository.findById(id);
            optionalCategory.ifPresent(category -> {
                try {
                    //Wrapping the file bytes inside a new Byte[] for the object
                    Byte[]objectBytes=new Byte[file.getBytes().length];
                    int i=0;
                    for (byte b:file.getBytes())
                        objectBytes[i++]=b;
                    category.setImage(objectBytes);
                    categoryRepository.save(category);
                } catch (IOException e) {
                    log.error("ERROR",e);
                    e.printStackTrace();
                }
            });

    }
}
