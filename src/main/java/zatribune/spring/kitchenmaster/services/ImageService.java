package zatribune.spring.kitchenmaster.services;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.data.entities.Category;

public interface ImageService {
    Mono<Category> saveImageFile(String id, FilePart filePart);
}
