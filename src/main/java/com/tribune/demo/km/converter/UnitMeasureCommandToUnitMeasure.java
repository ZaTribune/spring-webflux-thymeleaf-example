package com.tribune.demo.km.converter;


import org.bson.types.ObjectId;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.tribune.demo.km.command.UnitMeasureCommand;
import com.tribune.demo.km.data.entity.UnitMeasure;

@Component
public record UnitMeasureCommandToUnitMeasure() implements Converter<UnitMeasureCommand, UnitMeasure> {

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
