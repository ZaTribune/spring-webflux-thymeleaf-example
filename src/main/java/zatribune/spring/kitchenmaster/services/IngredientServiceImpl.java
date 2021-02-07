package zatribune.spring.kitchenmaster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import zatribune.spring.kitchenmaster.commands.IngredientCommand;
import zatribune.spring.kitchenmaster.converters.IngredientToIngredientCommand;
import zatribune.spring.kitchenmaster.data.entities.Ingredient;
import zatribune.spring.kitchenmaster.data.repositories.IngredientReactiveRepository;

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
