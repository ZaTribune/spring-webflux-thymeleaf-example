package zatribune.spring.cookmaster.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zatribune.spring.cookmaster.commands.CategoryCommand;
import zatribune.spring.cookmaster.data.entities.Category;

public interface CategoryService {
    Flux<Category> getAllCategories();
    Mono<Category> getCategoryById(String id);
    Mono<CategoryCommand> getCategoryCommandById(String id);
}
