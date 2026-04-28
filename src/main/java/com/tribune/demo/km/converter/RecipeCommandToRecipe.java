package com.tribune.demo.km.converter;

import org.bson.types.ObjectId;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.tribune.demo.km.command.RecipeCommand;
import com.tribune.demo.km.data.entity.Recipe;

import java.util.Objects;

@Component
public record RecipeCommandToRecipe(NotesCommandToNotes notesConverter,
                                    CategoryCommandToCategory categoryConverter,
                                    IngredientCommandToIngredient ingredientConverter) implements Converter<RecipeCommand, Recipe> {


    @Override
    public @NonNull Recipe convert(RecipeCommand source) {
        final Recipe recipe = new Recipe();
        if (source.getId() != null)//cases like creating a new recipe --> on the website
            recipe.setId(new ObjectId(source.getId()));
        recipe.setTitle(source.getTitle());
        recipe.setCookTime(source.getCookTime());
        recipe.setPrepTime(source.getPrepTime());
        recipe.setServings(source.getServings());
        recipe.setSource(source.getSource());
        if (source.getImage() != null && !source.getImage().isEmpty())
            recipe.setImage(source.getImage());
        recipe.setUrl(source.getUrl());
        recipe.setDirections(source.getDirections());
        recipe.setDifficulty(source.getDifficulty());
        if (source.getNotes() != null)
            recipe.setNotes(Objects.requireNonNull(notesConverter.convert(source.getNotes())));
        if (source.getCategories() != null && !source.getCategories().isEmpty())
            source.getCategories().forEach(c -> recipe.addCategory(Objects.requireNonNull(categoryConverter.convert(c))));
        if (source.getIngredients() != null && !source.getIngredients().isEmpty()) {
            //we filter who's not there anymore
            source.getIngredients().forEach(i -> recipe.addIngredient(Objects.requireNonNull(ingredientConverter.convert(i))));
        }
        return recipe;

    }
}
