package zatribune.spring.kitchenmaster.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import zatribune.spring.kitchenmaster.commands.CategoryCommand;
import zatribune.spring.kitchenmaster.converters.CategoryToCategoryCommand;
import zatribune.spring.kitchenmaster.services.CategoryService;


@Slf4j
@Controller
public class CategoriesController {
    private final CategoryService categoryService;
    private final CategoryToCategoryCommand categoryToCategoryCommand;

    @Autowired
    public CategoriesController(CategoryService categoryService, CategoryToCategoryCommand categoryToCategoryCommand) {
        this.categoryService = categoryService;
        this.categoryToCategoryCommand = categoryToCategoryCommand;
    }

    @RequestMapping("/categories")
    public String getCategoriesHomePage(Model model) {
        log.info("Categories Home");
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories/homeCategories";
    }

    @RequestMapping("/listCategories")
    public String listCategories(@RequestParam(required = false,defaultValue = "") String s, Model model) {
        log.info("listCategories: " + s);
        Flux<CategoryCommand> categories = categoryService.getAllCategories()
                .filter(category -> category.getDescription().startsWith(s))
                .map(categoryToCategoryCommand::convert)
                .limitRate(15);

        model.addAttribute("categoriesSuggestions", categories);
        return "categories/listCategories";
    }

    @RequestMapping("/showCategory/{id}")
    public String showCategory(@PathVariable String id, Model model){
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "categories/showCategory";
    }

    //todo:
    @PostMapping("/updateOrSaveCategory/{description}")
    public void updateOrSaveDescription(@PathVariable("description") String description){
        log.info("update category: {}", description);
    }

}
