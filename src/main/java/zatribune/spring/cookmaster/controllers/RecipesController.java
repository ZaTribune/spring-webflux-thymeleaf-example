package zatribune.spring.cookmaster.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import zatribune.spring.cookmaster.data.entities.Recipe;
import zatribune.spring.cookmaster.services.RecipeService;

import java.util.Optional;

@Controller
public class RecipesController {

    private final RecipeService recipeService;

    @Autowired
    public RecipesController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping("/recipes")
    public String getRecipesPage(Model model){
        return "/recipes/home";
    }

    @RequestMapping("/recipes/search")
    public String getRecipesSearchFragment(Model model){
        model.addAttribute("recipes",recipeService.getAllRecipes());
        return "/recipes/search";
    }

    @RequestMapping("/recipes/show/{id}")
    public String getRecipePage(@PathVariable String id,Model model){
        Optional<Recipe> recipe=recipeService.getRecipeById(Long.valueOf(id));
        recipe.ifPresent(r ->model.addAttribute("recipe",r));

        //Optional<Recipe>optionalRecipe=recipeService.getRecipeById()
        //model.addAttribute("recipe",recipeService.getRecipeById().get());
        return "/recipes/show";
    }

    @RequestMapping("/recipes/update/{id}")
    public String getUpdateRecipePage(@PathVariable String id, Model model){

        return "/recipes/update";

    }
}
