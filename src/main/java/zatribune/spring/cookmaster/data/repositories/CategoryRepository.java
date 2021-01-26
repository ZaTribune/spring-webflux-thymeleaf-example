package zatribune.spring.cookmaster.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zatribune.spring.cookmaster.data.entities.Category;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category,String> {
    Optional<Category> findCategoriesByDescription(String description);
}