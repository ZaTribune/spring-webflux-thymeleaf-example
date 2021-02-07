package zatribune.spring.kitchenmaster.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import zatribune.spring.kitchenmaster.services.IngredientService;

//todo: needs testing
//todo: no need for this controller right now.

@Slf4j
@Controller
public class IngredientsController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientsController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

/*
    @RequestMapping("/deleteIngredients")
    public @ResponseBody Boolean deleteIngredientRequest(@RequestParam("ids[]") String[] ids) {
        log.info("request delete ingredients "+ Arrays.toString(ids));
        for (String id : ids)
            ingredientService.deleteIngredientById(Long.valueOf(id));
        return true;
    }

 */
}
