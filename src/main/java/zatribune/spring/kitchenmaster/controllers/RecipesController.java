package zatribune.spring.kitchenmaster.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.commands.RecipeCommand;
import zatribune.spring.kitchenmaster.data.entities.Recipe;
import zatribune.spring.kitchenmaster.services.RecipeService;
import zatribune.spring.kitchenmaster.services.UnitMeasureService;

@Slf4j
@Controller
public class RecipesController {

    private final RecipeService recipeService;
    private final UnitMeasureService unitMeasureService;
    private WebDataBinder webDataBinder;

    @Autowired
    public RecipesController(RecipeService recipeService, UnitMeasureService unitMeasureService) {
        this.recipeService = recipeService;
        this.unitMeasureService = unitMeasureService;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        this.webDataBinder = webDataBinder;

    }

    @RequestMapping("/recipes")
    public String getRecipesHomePage() {
        return "recipes/homeRecipes";
    }

    @RequestMapping("/searchRecipes")
    public String searchRecipes(Model model) {
        model.addAttribute("recipes", recipeService.getAllRecipes());
        return "recipes/searchRecipes";
    }

    @RequestMapping("/showRecipe/{id}")
    public String showRecipe(@PathVariable String id, Model model){
        Mono<Recipe> recipe = recipeService.getRecipeById(id);
        model.addAttribute("recipe", recipe);
        return "recipes/showRecipe";
    }

    @RequestMapping("/createRecipe")
    public String createNewRecipe(Model model) {
        model.addAttribute("recipe", Mono.just(new RecipeCommand()));
        model.addAttribute("unitMeasures",unitMeasureService.getAllUnitMeasures());
        return "recipes/createRecipe";
    }

    @RequestMapping("/updateRecipe/{id}")
    public String updateRecipe(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.getRecipeCommandById(id));
        model.addAttribute("unitMeasures",unitMeasureService.getAllUnitMeasures());
        return "recipes/createRecipe";
    }


    @RequestMapping("/deleteRecipe/{id}")
    public @ResponseBody
    String deleteRecipe(@PathVariable String id) {
        log.info("deleting recipe: " + id);
        recipeService.deleteRecipeById(id);
        return "ok";
    }

    @PostMapping
    @RequestMapping("/updateOrSaveRecipe") // @ModelAttribute to get the attribute{recipe}
    // which we've passed to the view before--by default it will search for the name in the argument if
    //not specified in the annotation, except when there's no validation
    public String saveOrUpdateRecipe(@ModelAttribute("recipe") Mono<RecipeCommand> recipeCommand,Model model) {
        log.info("categories for recipe {}", recipeCommand);
        //we're doing a manual workaround there,we're getting a hand on the web data binder
        //and this is going to contain binding information of what was bound in this call
        webDataBinder.validate();
        BindingResult bindingResult = webDataBinder.getBindingResult();
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> log.error(objectError.toString()));
            model.addAttribute("unitMeasures",unitMeasureService.getAllUnitMeasures());//or it won't be rendered
            return "recipes/createRecipe";
        }
        //this annotation to tell spring to bind the form post parameters to the recipe
        //command object by the naming conventions of the properties automatically
        //this "redirect:" is a command that tells spring framework to redirect to a specific url
        recipeService.saveRecipeCommand(recipeCommand.block()).subscribe();//subscribe don't block
        return "recipes/showRecipe";
    }

//    @ModelAttribute("unitMeasures")
//    public Flux<UnitMeasureCommand>populateUOMList(){
//        //now, it will be bound to every request
//        return unitMeasureService.getAllUnitMeasures();
//    }
}
