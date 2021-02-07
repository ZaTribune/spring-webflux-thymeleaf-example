package zatribune.spring.kitchenmaster.converters;

import lombok.Synchronized;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import zatribune.spring.kitchenmaster.commands.IngredientCommand;
import zatribune.spring.kitchenmaster.data.entities.Ingredient;

@Component
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient> {

    private final UnitMeasureCommandToUnitMeasure unitMeasureConverter;

    @Autowired
    public IngredientCommandToIngredient(UnitMeasureCommandToUnitMeasure unitMeasureConverter) {
        this.unitMeasureConverter = unitMeasureConverter;
    }

    @Synchronized
    @Override
    public @NonNull Ingredient convert(IngredientCommand source) {
        final Ingredient ingredient = new Ingredient();
        if (source.getId() == null||source.getId().isEmpty())
            ingredient.setId(new ObjectId());
        else
            ingredient.setId(new ObjectId(source.getId()));
        ingredient.setAmount(source.getAmount());
        ingredient.setDescription(source.getDescription());
        ingredient.setUnitMeasure(unitMeasureConverter.convert(source.getUnitMeasure()));
        return ingredient;
    }
}
