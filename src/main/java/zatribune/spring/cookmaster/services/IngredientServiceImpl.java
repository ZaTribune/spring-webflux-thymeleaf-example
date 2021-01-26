package zatribune.spring.cookmaster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zatribune.spring.cookmaster.data.entities.Ingredient;
import zatribune.spring.cookmaster.data.entities.Recipe;
import zatribune.spring.cookmaster.data.repositories.IngredientRepository;
import zatribune.spring.cookmaster.exceptions.MyNotFoundException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository repository;

    @Autowired
    public IngredientServiceImpl(IngredientRepository repository) {
        this.repository = repository;
    }

    @Override
    public Ingredient getIngredientById(String id) {
        Optional<Ingredient> ingredient=repository.findById(id);
        if (ingredient.isEmpty()){
            throw new MyNotFoundException("Ingredient not found for id value: "+id);
        }
        return ingredient.get();
    }

    @Override
    public Set<Ingredient> getIngredientsByRecipe(Recipe recipe) {
        return StreamSupport.stream(repository.findAllByRecipe(recipe).spliterator(),false)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteIngredient(Ingredient ingredient) {
          repository.delete(ingredient);
    }

    @Override
    public void deleteIngredientById(String id) {
          repository.deleteById(id);
    }
}
