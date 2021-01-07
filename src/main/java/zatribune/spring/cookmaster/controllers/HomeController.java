package zatribune.spring.cookmaster.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

   @RequestMapping("/home")
   public String getHomePage(Model model){

       return "/home";
   }
}
