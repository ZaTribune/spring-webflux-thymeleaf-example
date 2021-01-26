package zatribune.spring.cookmaster.converters;

import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import zatribune.spring.cookmaster.commands.RecipeCommand;
import zatribune.spring.cookmaster.data.entities.Recipe;

import java.util.Base64;
import java.util.Objects;

@Component
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {
    private final NotesToNotesCommand notesConverter;
    private final CategoryToCategoryCommand categoryConverter;
    private final IngredientToIngredientCommand ingredientConverter;

    @Autowired
    public RecipeToRecipeCommand(NotesToNotesCommand notesConverter
            , CategoryToCategoryCommand categoryConverter
            , IngredientToIngredientCommand ingredientConverter) {
        this.notesConverter = notesConverter;
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
    }

    @Synchronized
    @Override
    public RecipeCommand convert(@Nullable Recipe source) {
        if (source == null)
            return null;
        final RecipeCommand recipeCommand = new RecipeCommand();
        if (source.getId() != null)
            recipeCommand.setId(source.getId().toString());
        recipeCommand.setTitle(source.getTitle());
        recipeCommand.setCookTime(source.getCookTime());
        recipeCommand.setPrepTime(source.getPrepTime());
        recipeCommand.setServings(source.getServings());
        recipeCommand.setSource(source.getSource());
        if (source.getImage() != null && !source.getImage().isEmpty())
            recipeCommand.setImage(source.getImage());
        recipeCommand.setUrl(source.getUrl());
        recipeCommand.setDirections(source.getDirections());
        recipeCommand.setDifficulty(source.getDifficulty());
        if (source.getNotes() != null)
            recipeCommand.setNotes(Objects.requireNonNull(notesConverter.convert(source.getNotes())));
        if (source.getCategories() != null && source.getCategories().size() > 0)
            source.getCategories().forEach(c ->
                    recipeCommand.getCategories().add(categoryConverter.convert(c))
            );
        if (source.getIngredients() != null && source.getIngredients().size() > 0)
            source.getIngredients().forEach(i ->
                    recipeCommand.getIngredients().add(Objects.requireNonNull(ingredientConverter.convert(i)))
            );

        return recipeCommand;

    }
}
