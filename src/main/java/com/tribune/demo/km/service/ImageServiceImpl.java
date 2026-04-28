package com.tribune.demo.km.service;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.data.entity.Category;
import com.tribune.demo.km.data.repository.CategoryReactiveRepository;

@Slf4j
@Service
public record ImageServiceImpl (CategoryReactiveRepository categoryRepository) implements ImageService {


    @Override
    public Mono<Category> saveImageFile(String id, FilePart filePart) {
        //both methods must be on the same level of hierarchy meaning that you just can't call save()
        //inside the findById() stream
        //File temp = new File(System.getProperty("user.dir") + "/temp/"+file.filename());
        //log.info(System.getProperty("user.dir") + "/temp/");
        //file.transferTo(temp);
        log.debug("file.filename():{}",filePart.filename());
        log.debug("file.name():{}",filePart.name());
        return categoryRepository.findById(new ObjectId(id)).flatMap(category -> {
            log.debug("found Entity with id :{}",category.getId());
            log.debug("old image length:{}",category.getImage() != null ? category.getImage().length : 0);
            byte[] bytes = filePart.content().flatMap(dataBuffer -> {
                log.debug("inside dataBuffer");
                log.debug("what could be read:{}" , dataBuffer.readableByteCount());
                log.debug("what could be written:{}" , dataBuffer.writableByteCount());
                byte[] unwrappedBytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(unwrappedBytes);
                log.debug("new image length:{}",unwrappedBytes.length);
                return Mono.just(unwrappedBytes);
            }).blockFirst();
            if (bytes != null) {
                category.setImage(bytes);
            }
            return categoryRepository.save(category);
        });
    }

}
