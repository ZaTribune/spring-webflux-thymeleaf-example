package zatribune.spring.kitchenmaster.converters;


import lombok.Synchronized;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import zatribune.spring.kitchenmaster.commands.UnitMeasureCommand;
import zatribune.spring.kitchenmaster.data.entities.UnitMeasure;

@Component
public class UnitMeasureCommandToUnitMeasure implements Converter<UnitMeasureCommand, UnitMeasure> {

    @Synchronized//thread safe
    @Override
    public
    UnitMeasure convert(@Nullable UnitMeasureCommand source) {
        final UnitMeasure unitMeasure = new UnitMeasure();
        if (source!=null) {
            if (source.getId() != null && !source.getId().isEmpty())
                unitMeasure.setId(new ObjectId(source.getId()));
            unitMeasure.setDescription(source.getDescription());
        }
        return unitMeasure;
    }
}
