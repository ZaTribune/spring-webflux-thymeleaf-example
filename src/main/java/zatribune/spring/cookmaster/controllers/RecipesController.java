package zatribune.spring.cookmaster.controllers;


import lombok.extern.slf4j.Slf4j;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import zatribune.spring.cookmaster.commands.RecipeCommand;
import zatribune.spring.cookmaster.converters.RecipeToRecipeCommand;
import zatribune.spring.cookmaster.data.entities.Recipe;
import zatribune.spring.cookmaster.services.RecipeService;
import zatribune.spring.cookmaster.services.UnitMeasureService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
    public String showRecipe(@PathVariable String id, Model model)throws NumberFormatException{
        Recipe recipe = recipeService.getRecipeById(Long.valueOf(id));
        model.addAttribute("recipe", recipe);
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
    public String updateRecipe(@PathVariable String id, Model model) throws NumberFormatException{
        Recipe recipe=recipeService.getRecipeById(Long.valueOf(id));
        model.addAttribute("recipe",recipeToRecipeCommand.convert(recipe));
        model.addAttribute("unitMeasures",unitMeasureService.getAllUnitMeasures());
        return "/recipes/createRecipe";
    }


    @RequestMapping("/deleteRecipe/{id}")
    public @ResponseBody String deleteRecipe(@PathVariable String id)throws NumberFormatException {
        log.info("deleting recipe: "+id);
        recipeService.deleteRecipeById(Long.valueOf(id));
        return "ok";
    }

    @PostMapping
    @RequestMapping("/updateOrSaveRecipe") // @ModelAttribute to get the attribute{recipe}
    // which we've passed to the view before--by default it will search for the name in the argument if
    //not specified in the annotation, except when there's no validation
    public String saveOrUpdateRecipe
            (@Valid @ModelAttribute("recipe") RecipeCommand recipeCommand, BindingResult bindingResult) {
        log.info("categories {} for recipe {}",recipeCommand.getCategories(),recipeCommand.getId());
        log.info("title {} for recipe",recipeCommand.getTitle());
        if (bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> log.error(objectError.toString()));
            return "/recipes/createRecipe";
        }
        //this annotation to tell spring to bind the form post parameters to the recipe
        //command object by the naming conventions of the properties automatically
        //this "redirect:" is a command that tells spring framework to redirect to a specific url
        recipeService.saveRecipeCommand(recipeCommand);
        return "/recipes/showRecipe";
    }

}
