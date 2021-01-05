package zatribune.spring.cookmaster.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import zatribune.spring.cookmaster.data.entities.Recipe;
import zatribune.spring.cookmaster.data.repositories.RecipeRepository;
import zatribune.spring.cookmaster.services.RecipeService;

import java.util.Optional;
import java.util.stream.StreamSupport;

@Controller
public class RecipeController {

    private final RecipeService recipeService;
    @Autowired
    public RecipeController(RecipeService recipeService){
        this.recipeService=recipeService;
    }


    @RequestMapping("/recipe/show/{id}")
    public String getRecipePage(@PathVariable String id,Model model){
        Optional<Recipe>recipe=recipeService.getRecipeById(Long.valueOf(id));
        recipe.ifPresent(r ->model.addAttribute("recipe",r));

        //Optional<Recipe>optionalRecipe=recipeService.getRecipeById()
        //model.addAttribute("recipe",recipeService.getRecipeById().get());
        return "/recipe/show";
    }
}
