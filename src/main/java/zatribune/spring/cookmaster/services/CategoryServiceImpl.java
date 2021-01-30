package zatribune.spring.cookmaster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zatribune.spring.cookmaster.commands.CategoryCommand;
import zatribune.spring.cookmaster.converters.CategoryToCategoryCommand;
import zatribune.spring.cookmaster.data.entities.Category;
import zatribune.spring.cookmaster.data.repositories.CategoryReactiveRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryReactiveRepository repository;
    private final CategoryToCategoryCommand categoryToCategoryCommand;

    @Autowired
    public CategoryServiceImpl(CategoryReactiveRepository repository,CategoryToCategoryCommand categoryToCategoryCommand){
        this.repository=repository;
        this.categoryToCategoryCommand=categoryToCategoryCommand;
    }


    @Override
    public Flux<Category> getAllCategories() {
        return repository.findAll();
    }

    @Override
    public Mono<Category> getCategoryById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<CategoryCommand> getCategoryCommandById(String id) {
        return getCategoryById(id).map(categoryToCategoryCommand::convert);
    }
}
