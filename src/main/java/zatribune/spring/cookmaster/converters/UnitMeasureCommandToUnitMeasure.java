package zatribune.spring.cookmaster.converters;


import lombok.NonNull;
import lombok.Synchronized;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zatribune.spring.cookmaster.commands.UnitMeasureCommand;
import zatribune.spring.cookmaster.data.entities.UnitMeasure;

@Component
public class UnitMeasureCommandToUnitMeasure implements Converter<UnitMeasureCommand, UnitMeasure> {

    @Synchronized//thread safe
    @Override
    public @NonNull UnitMeasure convert(UnitMeasureCommand source) {
        final UnitMeasure unitMeasure = new UnitMeasure();
        if (source.getId() != null&&!source.getId().isEmpty())
            unitMeasure.setId(new ObjectId(source.getId()));
        unitMeasure.setDescription(source.getDescription());
        return unitMeasure;
    }
}
