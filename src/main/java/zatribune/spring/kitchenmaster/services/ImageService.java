package zatribune.spring.kitchenmaster.services;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface ImageService {
    Mono<?> saveImageFile(String id, FilePart filePart);
}
