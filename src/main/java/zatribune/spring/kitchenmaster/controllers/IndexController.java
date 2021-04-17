package zatribune.spring.kitchenmaster.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.data.entities.User;

import java.util.List;

@Slf4j
@Controller
public class IndexController {

    @Autowired
    public IndexController() {

    }


    @RequestMapping({"", "/", "/index"})
    public Mono<String> getIndexPage(Model model, @RequestParam(required = false,defaultValue = "false")boolean logout) {
          //when deploying on a docker image, remove the leading slash->it has no use anyways but will fire errors on
//        //the running os,template might not exist or might not be accessible by any of the configured Template Resolvers]
        return ReactiveSecurityContextHolder.getContext().flatMap(securityContext -> {
                model.addAttribute("logout",logout);
            log.info(securityContext.getAuthentication().toString());
            return Mono.just("index");
        });
    }

    @RequestMapping("/modal/{type}")
    public Mono<String> getModal
            (@PathVariable ModalType type, Model model, @RequestParam(required = false) List<ObjectError> errors) {
        log.info("Now, I'm opening a modal of type {}", type);
        switch (type) {
            case LOGIN:
                model.addAttribute("user",new User());
                model.addAttribute("title", "Login");
                model.addAttribute("info", "Please, enter your credentials.");
                break;
            case INFO:
                model.addAttribute("title", "");
                model.addAttribute("info", "....some information");
                break;
            case DELETE:
                model.addAttribute("title", "Confirm Deleting a Recipe !");
                model.addAttribute("question", "Are you sure you want to delete this recipe?");
                model.addAttribute("info", "....some errors");
                break;
            case ERROR:
                model.addAttribute("title", null);
                model.addAttribute("errors", errors);
                model.addAttribute("info", "....some errors");
                break;
        }

        return Mono.just("fragments/modal");
    }


}
