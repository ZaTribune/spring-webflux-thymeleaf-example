package zatribune.spring.cookmaster.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import zatribune.spring.cookmaster.services.CategoryService;
import zatribune.spring.cookmaster.services.ImageService;

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
    String uploadImage(@PathVariable("id") String id,
                       @RequestParam("imageFile") MultipartFile multipartFile) {
        log.info("uploadImage() for category : {}",id);
        imageService.saveImageFile(id, multipartFile).block();
        return "NICE!!";
    }

//    @RequestMapping("/category/{id}/image")
//    public void showProductImage(@PathVariable String id, HttpServletResponse response)
//            throws IOException, MyNotFoundException {
//        //you may also want to consider running a HEAD request on the URL and getting the real content type from that.
//        //response.setContentType("image/jpeg"); // Or whatever format you wanna use
//        CategoryCommand categoryCommand = categoryService.getCategoryCommandById(id).block();
//        assert categoryCommand != null;
//        if (categoryCommand.getImage() != null) {
//            byte[] unboxedBytes = new byte[categoryCommand.getImage().length];
//            int i = 0;
//            for (Byte b : categoryCommand.getImage())
//                unboxedBytes[i++] = b;
//            InputStream is = new ByteArrayInputStream(unboxedBytes);
//            IOUtils.copy(is, response.getOutputStream());
//        }
//    }
}
