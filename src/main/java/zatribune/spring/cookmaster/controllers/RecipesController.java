package zatribune.spring.cookmaster.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zatribune.spring.cookmaster.commands.RecipeCommand;
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
    public String getRecipesHomePage(Model model) {
        return "/recipes/home";
    }

    @RequestMapping("/recipes/search")
    public String searchRecipes(Model model) {
        model.addAttribute("recipes", recipeService.getAllRecipes());
        return "/recipes/search";
    }

    @RequestMapping("/recipes/show/{id}")
    public String showRecipe(@PathVariable String id, Model model) {
        Optional<Recipe> recipe = recipeService.getRecipeById(Long.valueOf(id));
        recipe.ifPresent(r -> model.addAttribute("recipe", r));

        //Optional<Recipe>optionalRecipe=recipeService.getRecipeById()
        //model.addAttribute("recipe",recipeService.getRecipeById().get());
        return "/recipes/show";
    }

    @RequestMapping("/recipes/create")
    public String createNewRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        return "/recipes/create";

    }


    @PostMapping
    @RequestMapping("/recipes/update/")
    public String saveOrUpdateRecipe(@ModelAttribute RecipeCommand recipeCommand, Model model) {
        //this annotation to tell spring to bind the form post parameters to the recipe
        //command object by the naming conventions of the properties automatically
        RecipeCommand returnedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);

        //this is a command that tells spring framework to redirect to a specific url
        return "redirect:/recipes/show/" + returnedRecipeCommand.getId();

    }
}
