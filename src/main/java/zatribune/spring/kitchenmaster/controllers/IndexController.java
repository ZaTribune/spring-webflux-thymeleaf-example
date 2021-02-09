package zatribune.spring.kitchenmaster.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Controller
public class IndexController {

    @RequestMapping({"","/","/index"})
    public Mono<String> getIndexPage(Model model){
        log.debug("Now, I'm mapping the root page.");
        //when deploying on a docker image, remove the leading slash->it has no use anyways but will fire errors on
        //the running os,template might not exist or might not be accessible by any of the configured Template Resolvers]
        return Mono.just("index");
    }
    @RequestMapping("/modal/{type}")
    public Mono<String> getModal
            (@PathVariable ModalType type, Model model, @RequestParam(required = false)List<ObjectError>errors){
        log.info("Now, I'm opening a modal.");
        switch (type){
            case DELETE:
                model.addAttribute("title","Confirm Deleting a Recipe !");
                model.addAttribute("question","Are you sure you want to delete this recipe?");
                model.addAttribute("info","....some errors");
                break;
            case INFO:
                model.addAttribute("title","");
                model.addAttribute("info","....some information");
                break;
            case ERROR:
                model.addAttribute("title",null);
                model.addAttribute("errors",errors);
                model.addAttribute("info","....some errors");
                break;
        }

        return Mono.just("fragments/modal");
    }


}
