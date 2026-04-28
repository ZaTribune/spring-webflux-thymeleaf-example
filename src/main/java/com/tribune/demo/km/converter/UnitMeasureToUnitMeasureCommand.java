package com.tribune.demo.km.converter;

import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.tribune.demo.km.command.UnitMeasureCommand;
import com.tribune.demo.km.data.entity.UnitMeasure;

@Component
public record UnitMeasureToUnitMeasureCommand() implements Converter<UnitMeasure, UnitMeasureCommand> {

    @Override
    public @NonNull UnitMeasureCommand convert(UnitMeasure source) {
        final UnitMeasureCommand unitMeasure = new UnitMeasureCommand();
        if (source.getId() != null) unitMeasure.setId(source.getId().toString());
        unitMeasure.setDescription(source.getDescription());
        return unitMeasure;
    }
}
