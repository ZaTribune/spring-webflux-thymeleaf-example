package zatribune.spring.cookmaster.services;

import zatribune.spring.cookmaster.data.entities.Category;

import java.util.Optional;
import java.util.Set;

public interface CategoryService {
    Set<Category> getAllCategories();
    Optional<Category>getCategoryById(Long id);
}
