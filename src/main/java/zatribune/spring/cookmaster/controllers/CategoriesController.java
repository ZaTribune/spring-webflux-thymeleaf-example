package zatribune.spring.cookmaster.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import zatribune.spring.cookmaster.services.CategoryService;

@Controller
public class CategoriesController {

    private final CategoryService categoryService;

    public CategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping("/category")
    public String getCategoriesPage(Model model){
        model.addAttribute("categories",categoryService.getAllCategories());
        return "/category/all";
    }
}
