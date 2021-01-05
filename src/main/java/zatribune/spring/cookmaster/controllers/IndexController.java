package zatribune.spring.cookmaster.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import zatribune.spring.cookmaster.services.CategoryService;
import zatribune.spring.cookmaster.services.RecipeService;

@Slf4j
@Controller
public class IndexController {

    private final RecipeService recipeService;
    private final CategoryService categoryService;

    @Autowired
    public IndexController(RecipeService recipeService,CategoryService categoryService) {
        this.recipeService=recipeService;
        this.categoryService=categoryService;
    }

    @RequestMapping({"","/","/index"})
    public String getIndexPage(Model model){
        log.debug("Now, I'm mapping the root page.");
        model.addAttribute("recipes",recipeService.getAllRecipes());
        model.addAttribute("categories",categoryService.getAllCategories());
        return "index";
    }

}
