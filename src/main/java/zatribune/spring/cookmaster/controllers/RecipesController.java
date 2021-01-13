package zatribune.spring.cookmaster.controllers;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zatribune.spring.cookmaster.commands.CategoryCommand;
import zatribune.spring.cookmaster.commands.RecipeCommand;
import zatribune.spring.cookmaster.converters.RecipeToRecipeCommand;
import zatribune.spring.cookmaster.data.entities.Category;
import zatribune.spring.cookmaster.data.entities.Recipe;
import zatribune.spring.cookmaster.data.entities.UnitMeasure;
import zatribune.spring.cookmaster.services.RecipeService;
import zatribune.spring.cookmaster.services.UnitMeasureService;

import java.util.Base64;
import java.util.Optional;

@Slf4j
@Controller
public class RecipesController {

    private final RecipeService recipeService;
    private final RecipeToRecipeCommand recipeToRecipeCommand;
    private final UnitMeasureService unitMeasureService;

    @Autowired
    public RecipesController(RecipeService recipeService,RecipeToRecipeCommand recipeToRecipeCommand,UnitMeasureService unitMeasureService) {
        this.recipeService = recipeService;
        this.recipeToRecipeCommand=recipeToRecipeCommand;
        this.unitMeasureService=unitMeasureService;
    }

    @RequestMapping("/recipes")
    public String getRecipesHomePage(Model model) {
        return "/recipes/homeRecipes";
    }

    @RequestMapping("/searchRecipes")
    public String searchRecipes(Model model) {
        model.addAttribute("recipes", recipeService.getAllRecipes());
        return "/recipes/searchRecipes";
    }

    @RequestMapping("/showRecipe/{id}")
    public String showRecipe(@PathVariable String id, Model model) {
        Optional<Recipe> recipe = recipeService.getRecipeById(Long.valueOf(id));
        recipe.ifPresent(r -> model.addAttribute("recipe", r));

        //Optional<Recipe>optionalRecipe=recipeService.getRecipeById()
        //model.addAttribute("recipe",recipeService.getRecipeById().get());
        return "/recipes/showRecipe";
    }

    @RequestMapping("/createRecipe")
    public String createNewRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        model.addAttribute("unitMeasures",unitMeasureService.getAllUnitMeasures());
        return "/recipes/createRecipe";

    }

    @RequestMapping("/updateRecipe/{id}")
    public String updateRecipe(@PathVariable String id, Model model) {
        Optional<Recipe> optionalRecipe=recipeService.getRecipeById(Long.valueOf(id));
        optionalRecipe.ifPresent(element->model.addAttribute("recipe",recipeToRecipeCommand.convert(element)));
        model.addAttribute("unitMeasures",unitMeasureService.getAllUnitMeasures());
        return "/recipes/createRecipe";
    }


    @RequestMapping("/deleteRecipe/{id}")
    public @ResponseBody String deleteRecipe(@PathVariable String id, Model model) {
        log.info("deleting recipe: "+id);
        recipeService.deleteRecipeById(Long.valueOf(id));
        return "ok";
    }


    @PostMapping
    @RequestMapping(value = "/updateOrSaveRecipe") // @ModelAttribute to get the attribute{recipe}
    // which we've passed to the view before
    public @ResponseBody RecipeCommand saveOrUpdateRecipe(@ModelAttribute RecipeCommand recipeCommand,Model model) {
        log.info("categories "+recipeCommand.getCategories());
        log.info("image {}",recipeCommand.getImage());

        //this annotation to tell spring to bind the form post parameters to the recipe
        //command object by the naming conventions of the properties automatically
        RecipeCommand returnedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);
        System.out.println(recipeCommand);
        //this is a command that tells spring framework to redirect to a specific url
        return returnedRecipeCommand;
    }

}
