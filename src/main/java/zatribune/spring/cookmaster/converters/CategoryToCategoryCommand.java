package zatribune.spring.cookmaster.converters;

import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import zatribune.spring.cookmaster.commands.CategoryCommand;
import zatribune.spring.cookmaster.data.entities.Category;

@Component
public class CategoryToCategoryCommand implements Converter<Category, CategoryCommand> {

    @Synchronized
    @Override
    public @NonNull
    CategoryCommand convert(Category source) {
        final CategoryCommand categoryCommand = new CategoryCommand();
        if (source.getId() != null)
            categoryCommand.setId(source.getId().toString());
        categoryCommand.setDescription(source.getDescription());
        categoryCommand.setInfo(source.getInfo());
        categoryCommand.setImage(source.getImage());
        return categoryCommand;
    }
}
