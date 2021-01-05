package zatribune.spring.cookmaster.converters;

import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import zatribune.spring.cookmaster.commands.RecipeCommand;
import zatribune.spring.cookmaster.data.entities.Recipe;

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
        final RecipeCommand recipe = new RecipeCommand();
        recipe.setId(source.getId());
        recipe.setTitle(source.getTitle());
        recipe.setCookTime(source.getCookTime());
        recipe.setPrepTime(source.getPrepTime());
        recipe.setServings(source.getServings());
        recipe.setSource(source.getSource());
        recipe.setImage(source.getImage());
        recipe.setUrl(source.getUrl());
        recipe.setDirections(source.getDirections());
        recipe.setDifficulty(source.getDifficulty());
        if (source.getNotes() != null)
            recipe.setNotes(Objects.requireNonNull(notesConverter.convert(source.getNotes())));
        if (source.getCategories() != null && source.getCategories().size() > 0)
            source.getCategories().forEach(c ->
                    recipe.getCategories().add(categoryConverter.convert(c))
            );
        if (source.getIngredients() != null && source.getIngredients().size() > 0)
            source.getIngredients().forEach(i ->
                    recipe.getIngredients().add(Objects.requireNonNull(ingredientConverter.convert(i)))
            );

        return recipe;

    }
}
