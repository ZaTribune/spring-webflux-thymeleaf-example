package zatribune.spring.cookmaster.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import zatribune.spring.cookmaster.services.RecipeService;

@Controller
public class RecipesController {

    private final RecipeService recipeService;

    public RecipesController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping("/recipe")
    public String getRecipesPage(Model model){
        model.addAttribute("recipes",recipeService.getAllRecipes());
        return "/recipe/all";
    }
}
