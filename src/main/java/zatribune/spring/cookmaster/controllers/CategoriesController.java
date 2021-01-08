package zatribune.spring.cookmaster.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import zatribune.spring.cookmaster.data.entities.Category;
import zatribune.spring.cookmaster.services.CategoryService;

import java.util.Optional;

@Controller
public class CategoriesController {
    private final CategoryService categoryService;

    @Autowired
    public CategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping("/categories")
    public String getCategoriesHomePage(Model model){
        return "/categories/home";
    }

    @RequestMapping("/categories/search")
    public String searchCategories(Model model){
        model.addAttribute("categories",categoryService.getAllCategories());
        return "/categories/search";
    }

    @RequestMapping("/category/show/{id}")
    public String showCategory(@PathVariable String id, Model model){
        Optional<Category> optionalCategory=categoryService.getCategoryById(Long.valueOf(id));
        optionalCategory.ifPresent(category ->model.addAttribute("category",category));
        return "/categories/show";
    }
}
