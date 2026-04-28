package com.tribune.demo.km.converter;

import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.tribune.demo.km.command.IngredientCommand;
import com.tribune.demo.km.data.entity.Ingredient;

@Component
public record IngredientToIngredientCommand(
        UnitMeasureToUnitMeasureCommand unitMeasureConverter) implements Converter<Ingredient, IngredientCommand> {


    @Override
    public @NonNull IngredientCommand convert(Ingredient source) {
        final IngredientCommand ingredient = new IngredientCommand();
        if (source.getId() != null) ingredient.setId(source.getId().toString());
        ingredient.setAmount(source.getAmount());
        ingredient.setDescription(source.getDescription());
        if (source.getUnitMeasure() != null)
            ingredient.setUnitMeasure(unitMeasureConverter.convert(source.getUnitMeasure()));
        return ingredient;
    }
}
