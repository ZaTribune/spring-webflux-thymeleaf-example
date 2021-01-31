package zatribune.spring.cookmaster.controllers;



import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import zatribune.spring.cookmaster.commands.CategoryCommand;
import zatribune.spring.cookmaster.converters.CategoryToCategoryCommand;
import zatribune.spring.cookmaster.data.entities.Category;
import zatribune.spring.cookmaster.services.CategoryService;
import java.io.IOException;
import java.util.*;


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
    public String listCategories(@RequestParam(required = false) String s, Model model) {
        log.info("listCategories: " + s);
        Flux<CategoryCommand> categories = categoryService.getAllCategories()
                .map(categoryToCategoryCommand::convert)
                .filter(category -> category.getDescription().startsWith(s))
                .limitRate(15);

        model.addAttribute("categoriesSuggestions", categories);
        return "categories/listCategories";
    }

    @RequestMapping("/showCategory/{id}")
    public String showCategory(@PathVariable String id, Model model){
        Category category = categoryService.getCategoryById(id).block();
        model.addAttribute("category", category);
        return "categories/showCategory";
    }

    //todo:
    @PostMapping("/updateOrSaveCategory/{description}")
    public void updateOrSaveDescription(@PathVariable("description") String description) throws IOException {
        log.info("update category: {}", description);
    }

}
