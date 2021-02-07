package zatribune.spring.kitchenmaster.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.services.CategoryService;
import zatribune.spring.kitchenmaster.services.ImageService;

@Slf4j
@Controller
public class ImageController {

    private final ImageService imageService;
    private final CategoryService categoryService;

    @Autowired
    public ImageController(ImageService imageService, CategoryService categoryService) {
        this.imageService = imageService;
        this.categoryService = categoryService;
    }

    @PostMapping("/category/{id}/uploadImage")
    public @ResponseBody
    Boolean uploadImage(@PathVariable("id") String id,
                           @RequestPart("imageFile") FilePart filePart) {
        log.info("uploadImage() for category : {}",id);
        //can't block a mono as a parameter nor as a return
        imageService.saveImageFile(id,filePart).subscribe();  //subscribe means listen for the block
        return Boolean.TRUE;
    }

    @RequestMapping("/category/{id}/image/{timestamp}")
    @ResponseBody
    public Mono<byte[]> showProductImage(@PathVariable String id,@PathVariable(required = false) String timestamp){
        log.info("img timestamp:{}",timestamp);
        return categoryService.getCategoryCommandById(id).flatMap(categoryCommand -> {
            //todo: on this case, do we really need unboxing??
            byte[] unboxedBytes = new byte[0];
            if (categoryCommand.getImage() != null&&categoryCommand.getImage().length>0) {
                unboxedBytes = new byte[categoryCommand.getImage().length];
                int i = 0;
                for (Byte b : categoryCommand.getImage())
                    unboxedBytes[i++] = b;
            }
            return Mono.just(unboxedBytes);
        });
    }
}
