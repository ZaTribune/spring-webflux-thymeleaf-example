package com.tribune.demo.km.converter;


import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.tribune.demo.km.command.CategoryCommand;
import com.tribune.demo.km.data.entity.Category;

@Component
public record CategoryCommandToCategory() implements Converter<CategoryCommand, Category> {

    @Override
    public @NonNull Category convert(CategoryCommand source) {
        final Category category = new Category();
        if (source.getId() != null && !source.getId().isEmpty())
            category.setId(new ObjectId(source.getId()));
        category.setDescription(source.getDescription());
        category.setInfo(source.getInfo());
        category.setImage(source.getImage());
        return category;
    }
}
