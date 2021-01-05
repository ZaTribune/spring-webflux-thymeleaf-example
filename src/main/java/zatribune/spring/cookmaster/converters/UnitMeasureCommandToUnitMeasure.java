package zatribune.spring.cookmaster.converters;


import lombok.NonNull;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import zatribune.spring.cookmaster.commands.UnitMeasureCommand;
import zatribune.spring.cookmaster.data.entities.UnitMeasure;

@Component
public class UnitMeasureCommandToUnitMeasure implements Converter<UnitMeasureCommand, UnitMeasure> {

    @Synchronized//thread safe
    @Override
    public UnitMeasure convert(@Nullable UnitMeasureCommand source) {
        if (source==null)
            return null;
        final UnitMeasure unitMeasure=new UnitMeasure();
        unitMeasure.setId(source.getId());
        unitMeasure.setDescription(source.getDescription());
        return unitMeasure;
    }
}
