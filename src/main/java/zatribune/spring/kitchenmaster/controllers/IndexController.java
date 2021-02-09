package zatribune.spring.kitchenmaster.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
public class IndexController {

    @RequestMapping({"","/","/index"})
    public String getIndexPage(Model model){
        log.debug("Now, I'm mapping the root page.");
        //when deploying on a docker image, remove the leading slash->it has no use anyways but will fire errors on
        //the running os,template might not exist or might not be accessible by any of the configured Template Resolvers]
        return "index";
    }
    @RequestMapping("/modal/{type}")
    public String getModal
            (@PathVariable String type, Model model, @RequestParam(required = false)List<ObjectError>errors){
        log.info("Now, I'm opening a modal.");
        switch (type){
            case "delete":
                model.addAttribute("title","Info");
                model.addAttribute("question","Are you sure you want to delete this recipe?");
                model.addAttribute("info","....some errors");
                break;
            case "info":
                model.addAttribute("title","");
                model.addAttribute("info","....some information");
                break;
            case "error":
                model.addAttribute("title",null);
                model.addAttribute("errors",errors);
                model.addAttribute("info","....some errors");
                break;
        }

        return "fragments/modal";
    }


}
