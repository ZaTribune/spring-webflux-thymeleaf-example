package zatribune.spring.cookmaster.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import zatribune.spring.cookmaster.services.ImageService;

@Slf4j
@Controller
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/category/{id}/uploadImage")
    public @ResponseBody String uploadImage(@PathVariable("id") String id, @RequestParam("imageFile") MultipartFile multipartFile){
        log.info("xxxxx");
        imageService.saveImageFile(Long.valueOf(id),multipartFile);
        return "NICE!!";
    }
}
