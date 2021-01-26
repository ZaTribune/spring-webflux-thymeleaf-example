package zatribune.spring.cookmaster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zatribune.spring.cookmaster.commands.CategoryCommand;
import zatribune.spring.cookmaster.converters.CategoryToCategoryCommand;
import zatribune.spring.cookmaster.data.entities.Category;
import zatribune.spring.cookmaster.data.repositories.CategoryRepository;
import zatribune.spring.cookmaster.exceptions.MyNotFoundException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final CategoryToCategoryCommand categoryToCategoryCommand;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository,CategoryToCategoryCommand categoryToCategoryCommand){
        this.repository=repository;
        this.categoryToCategoryCommand=categoryToCategoryCommand;
    }


    @Override
    public Set<Category> getAllCategories() {
        return StreamSupport.stream(repository.findAll().spliterator(),false).collect(Collectors.toSet());
    }

    @Override
    public Category getCategoryById(String id) {
        //System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        Optional<Category>optionalCategory=repository.findById(id);
        if (optionalCategory.isEmpty()){
            throw new MyNotFoundException("Category not found for id value: "+id);
        }
        return optionalCategory.get();
    }

    @Override
    public CategoryCommand getCategoryCommandById(String id) {
        return categoryToCategoryCommand.convert(getCategoryById(id));
    }
}
