package zatribune.spring.kitchenmaster.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import zatribune.spring.kitchenmaster.commands.UnitMeasureCommand;
import zatribune.spring.kitchenmaster.data.entities.UnitMeasure;

@Component
public class UnitMeasureToUnitMeasureCommand implements Converter<UnitMeasure, UnitMeasureCommand> {
    @Synchronized//thread safe
    @Override
    public @NonNull
    UnitMeasureCommand convert(UnitMeasure source) {
        final UnitMeasureCommand unitMeasure = new UnitMeasureCommand();
        if (source.getId() != null)
            unitMeasure.setId(source.getId().toString());
        unitMeasure.setDescription(source.getDescription());
        return unitMeasure;
    }
}
