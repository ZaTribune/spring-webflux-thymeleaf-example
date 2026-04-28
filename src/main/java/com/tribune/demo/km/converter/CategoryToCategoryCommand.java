package com.tribune.demo.km.converter;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.tribune.demo.km.command.CategoryCommand;
import com.tribune.demo.km.data.entity.Category;

@Component
public record CategoryToCategoryCommand() implements Converter<Category, CategoryCommand> {

    @Override
    public @NonNull
    CategoryCommand convert(@Nullable Category source) {
        final CategoryCommand categoryCommand = new CategoryCommand();
        if (source != null) {
            if (source.getId() != null)
                categoryCommand.setId(source.getId().toString());
            categoryCommand.setDescription(source.getDescription());
            categoryCommand.setInfo(source.getInfo());
            categoryCommand.setImage(source.getImage());
        }
        return categoryCommand;
    }
}
