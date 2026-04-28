package com.tribune.demo.km.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.service.CategoryService;
import com.tribune.demo.km.service.ImageService;

@Slf4j
@Controller
public record ImageController(ImageService imageService, CategoryService categoryService) {


    // REST standard: POST /categories/{id}/image
    @PostMapping("/categories/{id}/image")
    public @ResponseBody
    Mono<Boolean> uploadImage(@PathVariable("id") String id,
                              @RequestPart("imageFile") FilePart filePart) {
        log.info("POST /categories/{}/image - Upload image for category: {}", id, id);
        return imageService.saveImageFile(id, filePart)
                .thenReturn(Boolean.TRUE)
                .doOnError(e -> log.error("Failed to save image for category: {}", id, e))
                .onErrorReturn(Boolean.FALSE);
    }

    // REST standard: GET /categories/{id}/image
    @GetMapping("/categories/{id}/image")
    @ResponseBody
    public Mono<byte[]> getCategoryImage(@PathVariable String id) {
        log.info("GET /categories/{}/image - Get image for category: {}", id, id);
        return categoryService.getCategoryCommandById(id).flatMap(categoryCommand -> {
            byte[] imageBytes = categoryCommand.getImage();
            if (imageBytes == null) {
                imageBytes = new byte[0];
            }
            return Mono.just(imageBytes);
        });
    }
}
