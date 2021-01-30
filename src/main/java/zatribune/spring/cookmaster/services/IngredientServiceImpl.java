package zatribune.spring.cookmaster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import zatribune.spring.cookmaster.commands.IngredientCommand;
import zatribune.spring.cookmaster.converters.IngredientToIngredientCommand;
import zatribune.spring.cookmaster.data.entities.Ingredient;
import zatribune.spring.cookmaster.data.repositories.IngredientReactiveRepository;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientReactiveRepository repository;
    private final IngredientToIngredientCommand converter;

    @Autowired
    public IngredientServiceImpl(IngredientReactiveRepository repository,IngredientToIngredientCommand converter) {
        this.repository = repository;
        this.converter=converter;
    }

    @Override
    public Mono<IngredientCommand> getIngredientById(String id) {
        return repository.findById(id).map(converter::convert);
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
