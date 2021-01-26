package zatribune.spring.cookmaster.converters;


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
    public Category convert(@Nullable CategoryCommand source) {
        if(source==null)
        return null;
        final Category category=new Category();
        category.setId(new ObjectId(source.getId()));
        category.setDescription(source.getDescription());
        category.setInfo(source.getInfo());
        category.setImage(source.getImage());
        return category;
    }
}
