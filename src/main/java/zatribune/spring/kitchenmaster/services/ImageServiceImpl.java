package zatribune.spring.kitchenmaster.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.data.entities.Category;
import zatribune.spring.kitchenmaster.data.repositories.CategoryReactiveRepository;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final CategoryReactiveRepository categoryRepository;

    @Autowired
    public ImageServiceImpl(CategoryReactiveRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Mono<Category> saveImageFile(String id, FilePart filePart) {
        //both methods must be on the same level of hierarchy meaning that you just can't call save()
        //inside the findById() stream
        //File temp = new File(System.getProperty("user.dir") + "/temp/"+file.filename());
        //log.info(System.getProperty("user.dir") + "/temp/");
        //file.transferTo(temp);
        log.debug("file.filename():{}",filePart.filename());
        log.debug("file.name():{}",filePart.name());
        return categoryRepository.findById(id).map(category -> {
            log.debug("found Entity with id :{}",category.getId());
            log.debug("old image length:{}",category.getImage().length);
            byte[] bytes = filePart.content().flatMap(dataBuffer -> {
                log.debug("inside dataBuffer");
                log.debug("what could be read:{}" , dataBuffer.readableByteCount());
                log.debug("what could be written:{}" , dataBuffer.writableByteCount());
                byte[] unwrappedBytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(unwrappedBytes);
                log.debug("new image length:{}",unwrappedBytes.length);
                return Mono.just(unwrappedBytes);
                /*
                this way returned null bytes which will affect showImage() function
                Byte[]objectBytes=new Byte[dataBuffer.readableByteCount()];
                for (int i = 0; i < dataBuffer.readableByteCount(); i++) {
                objectBytes[i] = dataBuffer.read();
                }
                */
            }).blockFirst();
            if (bytes!=null){
                Byte[] objectBytes = new Byte[bytes.length];
                for (int i = 0; i < bytes.length; i++) {
                    objectBytes[i] = bytes[i];
                }
                category.setImage(objectBytes);
                categoryRepository.save(category).subscribe();
            }
            return category;
        });
    }

}
