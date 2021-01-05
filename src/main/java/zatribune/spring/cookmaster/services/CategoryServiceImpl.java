package zatribune.spring.cookmaster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zatribune.spring.cookmaster.data.entities.Category;
import zatribune.spring.cookmaster.data.repositories.CategoryRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository){
        this.repository=repository;
    }


    @Override
    public Set<Category> getAllCategories() {
        return StreamSupport.stream(repository.findAll().spliterator(),false).collect(Collectors.toSet());
    }

    @Override
    public Optional<Category> getCategoryById(Long id) {
        return repository.findById(id);
    }
}
