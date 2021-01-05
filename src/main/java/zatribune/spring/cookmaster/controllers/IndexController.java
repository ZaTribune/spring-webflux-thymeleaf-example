package zatribune.spring.cookmaster.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class IndexController {

    @RequestMapping({"","/","/index"})
    public String getIndexPage(Model model){
        log.debug("Now, I'm mapping the root page.");

        return "index";
    }

}
