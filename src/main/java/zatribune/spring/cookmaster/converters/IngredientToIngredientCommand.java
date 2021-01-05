package zatribune.spring.cookmaster.converters;

import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import zatribune.spring.cookmaster.commands.IngredientCommand;
import zatribune.spring.cookmaster.data.entities.Ingredient;

@Component
public class IngredientToIngredientCommand implements Converter<Ingredient, IngredientCommand> {
    private final UnitMeasureToUnitMeasureCommand unitMeasureConverter;

    @Autowired
    public IngredientToIngredientCommand(UnitMeasureToUnitMeasureCommand unitMeasureConverter) {
        this.unitMeasureConverter = unitMeasureConverter;
    }

    @Synchronized
    @Override
    public IngredientCommand convert(@Nullable Ingredient source) {
        if (source==null)
            return null;
        final IngredientCommand ingredient=new IngredientCommand();
        ingredient.setId(source.getId());
        ingredient.setAmount(source.getAmount());
        ingredient.setDescription(source.getDescription());
        ingredient.setUnitMeasure(unitMeasureConverter.convert(source.getUnitMeasure()));
        return ingredient;
    }
}
