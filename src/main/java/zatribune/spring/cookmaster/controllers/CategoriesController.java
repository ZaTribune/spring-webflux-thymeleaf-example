package zatribune.spring.cookmaster.controllers;


import jdk.jfr.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.ContentTypeUtils;
import zatribune.spring.cookmaster.commands.CategoryCommand;
import zatribune.spring.cookmaster.converters.CategoryToCategoryCommand;
import zatribune.spring.cookmaster.data.entities.Category;
import zatribune.spring.cookmaster.exceptions.MyNotFoundException;
import zatribune.spring.cookmaster.services.CategoryService;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    public String showCategory(@PathVariable String id, Model model) throws NumberFormatException{
        Optional<Category> optionalCategory = categoryService.getCategoryById(Long.valueOf(id));
        optionalCategory.ifPresent(category -> model.addAttribute("category", category));
        return "/categories/showCategory";
    }

    @PostMapping("/updateOrSaveCategory")
    public void updateOrSaveDescription(@PathParam("description") String description, HttpServletResponse response) throws IOException {
        log.info("update category: {}", description);
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
        //this will be sent as data to the ajax
        response.getWriter().write("success");
    }

}
