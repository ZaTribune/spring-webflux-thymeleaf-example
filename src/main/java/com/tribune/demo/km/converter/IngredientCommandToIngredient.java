package com.tribune.demo.km.converter;

import org.bson.types.ObjectId;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.tribune.demo.km.command.IngredientCommand;
import com.tribune.demo.km.data.entity.Ingredient;

@Component
public record IngredientCommandToIngredient(
        UnitMeasureCommandToUnitMeasure unitMeasureConverter) implements Converter<IngredientCommand, Ingredient> {


    @Override
    public @NonNull Ingredient convert(IngredientCommand source) {
        final Ingredient ingredient = new Ingredient();
        if (source.getId() == null || source.getId().isEmpty()) ingredient.setId(new ObjectId());
        else ingredient.setId(new ObjectId(source.getId()));
        ingredient.setAmount(source.getAmount());
        ingredient.setDescription(source.getDescription());
        ingredient.setUnitMeasure(unitMeasureConverter.convert(source.getUnitMeasure()));
        return ingredient;
    }
}
