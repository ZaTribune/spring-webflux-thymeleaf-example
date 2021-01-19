package zatribune.spring.cookmaster.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zatribune.spring.cookmaster.commands.CategoryCommand;
import zatribune.spring.cookmaster.converters.CategoryToCategoryCommand;
import zatribune.spring.cookmaster.data.entities.Category;
import zatribune.spring.cookmaster.services.CategoryService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class CategoriesController {
    private final CategoryService categoryService;
    private final CategoryToCategoryCommand categoryToCategoryCommand;

    @Autowired
    public CategoriesController(CategoryService categoryService,CategoryToCategoryCommand categoryToCategoryCommand) {
        this.categoryService = categoryService;
        this.categoryToCategoryCommand = categoryToCategoryCommand;
    }

    @RequestMapping("/categories")
    public String getCategoriesHomePage(Model model) {
        log.info("Categories Home");
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/categories/homeCategories";
    }

    @RequestMapping("/listCategories")
    public String listCategories(@RequestParam(required = false) String s, Model model) {
        log.info("listCategories: " + s);
        Set<CategoryCommand> categories = categoryService.getAllCategories().stream()
                .map(categoryToCategoryCommand::convert)
                .limit(15)
                .filter(Objects::nonNull)
                .filter(category -> category.getDescription().startsWith(s))
                .collect(Collectors.toSet());
        model.addAttribute("categoriesSuggestions", categories);
        return "/categories/listCategories";
    }

    @RequestMapping("/showCategory/{id}")
    public String showCategory(@PathVariable String id, Model model) {
        Optional<Category> optionalCategory = categoryService.getCategoryById(Long.valueOf(id));
        optionalCategory.ifPresent(category -> model.addAttribute("category", category));
        return "/categories/showCategory";
    }
}
