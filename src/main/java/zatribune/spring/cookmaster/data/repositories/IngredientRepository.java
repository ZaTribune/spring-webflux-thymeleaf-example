package zatribune.spring.cookmaster.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zatribune.spring.cookmaster.data.entities.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient,Long> {
}
