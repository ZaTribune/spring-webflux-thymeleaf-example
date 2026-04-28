package com.tribune.demo.km.converter;

import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.tribune.demo.km.command.RecipeCommand;
import com.tribune.demo.km.data.entity.Recipe;

import java.util.Objects;

@Component
public record RecipeToRecipeCommand(NotesToNotesCommand notesConverter, CategoryToCategoryCommand categoryConverter,
                                    IngredientToIngredientCommand ingredientConverter) implements Converter<Recipe, RecipeCommand> {

    @Override
    public @NonNull RecipeCommand convert(Recipe source) {
        final RecipeCommand recipeCommand = new RecipeCommand();
        if (source.getId() != null) recipeCommand.setId(source.getId().toString());
        recipeCommand.setTitle(source.getTitle());
        recipeCommand.setCookTime(source.getCookTime());
        recipeCommand.setPrepTime(source.getPrepTime());
        recipeCommand.setServings(source.getServings());
        recipeCommand.setSource(source.getSource());
        if (source.getImage() != null && !source.getImage().isEmpty()) recipeCommand.setImage(source.getImage());
        recipeCommand.setUrl(source.getUrl());
        recipeCommand.setDirections(source.getDirections());
        recipeCommand.setDifficulty(source.getDifficulty());
        if (source.getNotes() != null)
            recipeCommand.setNotes(Objects.requireNonNull(notesConverter.convert(source.getNotes())));
        if (source.getCategories() != null && !source.getCategories().isEmpty())
            source.getCategories().forEach(c -> recipeCommand.getCategories().add(categoryConverter.convert(c)));
        if (source.getIngredients() != null && !source.getIngredients().isEmpty())
            source.getIngredients().forEach(i -> recipeCommand.getIngredients().add(Objects.requireNonNull(ingredientConverter.convert(i))));

        return recipeCommand;

    }
}
