package zatribune.spring.cookmaster.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zatribune.spring.cookmaster.data.entities.Recipe;

import java.util.Optional;


public interface RecipeRepository extends CrudRepository<Recipe,Long> {
    Optional<Recipe> findRecipeByTitle(String title);
}
