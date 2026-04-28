package com.tribune.demo.km.service;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.data.entity.Category;

public interface ImageService {
    Mono<Category> saveImageFile(String id, FilePart filePart);
}
