package zatribune.spring.cookmaster.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zatribune.spring.cookmaster.data.entities.Ingredient;
import zatribune.spring.cookmaster.data.entities.Recipe;

import java.util.Optional;

public interface IngredientRepository extends CrudRepository<Ingredient,String> {
    Iterable<Ingredient> findAllByRecipe(Recipe recipe);
}
