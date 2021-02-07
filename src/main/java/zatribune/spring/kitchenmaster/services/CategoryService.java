package zatribune.spring.kitchenmaster.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.commands.CategoryCommand;
import zatribune.spring.kitchenmaster.data.entities.Category;

public interface CategoryService {
    Flux<Category> getAllCategories();
    Mono<Category> getCategoryById(String id);
    Mono<CategoryCommand> getCategoryCommandById(String id);
}
