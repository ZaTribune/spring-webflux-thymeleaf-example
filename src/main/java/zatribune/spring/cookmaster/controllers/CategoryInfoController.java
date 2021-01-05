package zatribune.spring.cookmaster.controllers;

import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import zatribune.spring.cookmaster.data.entities.Category;
import zatribune.spring.cookmaster.services.CategoryService;

import java.util.Optional;

@Controller
public class CategoryInfoController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryInfoController(CategoryService categoryService){
        this.categoryService=categoryService;
    }

    @RequestMapping("/category/show/{id}")
    public String getCategoryPage(@PathVariable String id, Model model){
        Optional<Category>optionalCategory=categoryService.getCategoryById(Long.valueOf(id));
        optionalCategory.ifPresent(category ->model.addAttribute("category",category));
        return "/category/show";
    }
}
