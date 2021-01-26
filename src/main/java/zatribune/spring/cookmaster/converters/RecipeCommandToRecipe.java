package zatribune.spring.cookmaster.converters;

import lombok.Synchronized;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import zatribune.spring.cookmaster.commands.RecipeCommand;
import zatribune.spring.cookmaster.data.entities.Ingredient;
import zatribune.spring.cookmaster.data.entities.Recipe;

import java.util.Base64;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {
    NotesCommandToNotes notesConverter;
    CategoryCommandToCategory categoryConverter;
    IngredientCommandToIngredient ingredientConverter;

    @Autowired
    public RecipeCommandToRecipe(NotesCommandToNotes notesConverter
            , CategoryCommandToCategory categoryConverter
            , IngredientCommandToIngredient ingredientConverter) {
        this.notesConverter = notesConverter;
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
    }

    @Synchronized
    @Override
    public Recipe convert(@Nullable RecipeCommand source) {
        if (source == null)
            return null;
        final Recipe recipe = new Recipe();
        recipe.setId(new ObjectId(source.getId()));
        recipe.setTitle(source.getTitle());
        recipe.setCookTime(source.getCookTime());
        recipe.setPrepTime(source.getPrepTime());
        recipe.setServings(source.getServings());
        recipe.setSource(source.getSource());
        if (source.getImage()!=null&&!source.getImage().isEmpty())
            recipe.setImage(source.getImage());
        recipe.setUrl(source.getUrl());
        recipe.setDirections(source.getDirections());
        recipe.setDifficulty(source.getDifficulty());
        if (source.getNotes() != null)
            recipe.setNotes(Objects.requireNonNull(notesConverter.convert(source.getNotes())));
        if (source.getCategories() != null && source.getCategories().size() > 0)
            source.getCategories().forEach(c -> recipe.getCategories().add(categoryConverter.convert(c)));
        if (source.getIngredients() != null && source.getIngredients().size() > 0) {
            //we filter who's not there any more
            source.getIngredients().forEach(i -> recipe.addIngredient(Objects.requireNonNull(ingredientConverter.convert(i))));
        }
        return recipe;

    }
}
