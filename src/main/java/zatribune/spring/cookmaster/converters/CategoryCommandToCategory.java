package zatribune.spring.cookmaster.converters;


import lombok.NonNull;
import lombok.Synchronized;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import zatribune.spring.cookmaster.commands.CategoryCommand;
import zatribune.spring.cookmaster.data.entities.Category;

@Component
public class CategoryCommandToCategory implements Converter<CategoryCommand, Category> {

    @Synchronized
    @Override
    public @NonNull Category convert(CategoryCommand source) {
        final Category category = new Category();
        if (source.getId() != null&&!source.getId().isEmpty())
            category.setId(new ObjectId(source.getId()));
        category.setDescription(source.getDescription());
        category.setInfo(source.getInfo());
        category.setImage(source.getImage());
        return category;
    }
}
