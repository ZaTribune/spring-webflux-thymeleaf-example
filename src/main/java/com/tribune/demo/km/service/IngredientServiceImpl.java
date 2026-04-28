package com.tribune.demo.km.service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.command.IngredientCommand;
import com.tribune.demo.km.converter.IngredientToIngredientCommand;
import com.tribune.demo.km.data.entity.Ingredient;
import com.tribune.demo.km.data.repository.IngredientReactiveRepository;

@Service
public record IngredientServiceImpl(IngredientReactiveRepository repository, IngredientToIngredientCommand converter)
        implements IngredientService {


    @Override
    public Mono<IngredientCommand> getIngredientById(String id) {
        return repository.findById(new ObjectId(id)).map(converter::convert);
    }

    @Override
    public Mono<Void> deleteIngredient(Ingredient ingredient) {
        return repository.delete(ingredient);
    }

    @Override
    public Mono<Void> deleteIngredientById(String id) {
        return repository.deleteById(new ObjectId(id));
    }
}
