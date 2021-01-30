package zatribune.spring.cookmaster.converters;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zatribune.spring.cookmaster.commands.CategoryCommand;
import zatribune.spring.cookmaster.commands.IngredientCommand;
import zatribune.spring.cookmaster.commands.NotesCommand;
import zatribune.spring.cookmaster.commands.RecipeCommand;
import zatribune.spring.cookmaster.data.entities.*;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RecipeCommandToRecipeTest {
    private RecipeCommandToRecipe recipeCommandToRecipeConverter;
    private final String title="a dummy recipe title";
    private final Integer prepTime=10;
    private final Integer cookTime=20;
    private final Integer servings=5;
    private final String source="a dummy recipe source";
    private final String url="a dummy recipe url";
    private final String directions="a dummy recipe directions";
    private final Difficulty difficulty=Difficulty.MODERATE;
    private final String image="a dummy recipe image";
    private NotesCommand notes;
    private final String descriptionNotes="a dummy notes description";
    private CategoryCommand category;
    private final String descriptionCategory="a dummy Category description";
    private IngredientCommand ingredient1;
    private IngredientCommand ingredient2;

    @BeforeEach
    void setUp() {
        recipeCommandToRecipeConverter =new RecipeCommandToRecipe(
                new NotesCommandToNotes()
                ,new CategoryCommandToCategory()
                ,new IngredientCommandToIngredient(new UnitMeasureCommandToUnitMeasure())
        );
    }

    @Test
    void testEmptyObject(){
        assertNotNull(recipeCommandToRecipeConverter.convert(new RecipeCommand()));
    }

    @Test
    void convert() {
        RecipeCommand recipeCommand=new RecipeCommand();
        recipeCommand.setId(new ObjectId().toString());
        recipeCommand.setTitle(title);
        recipeCommand.setCookTime(cookTime);
        recipeCommand.setPrepTime(prepTime);
        recipeCommand.setServings(servings);
        recipeCommand.setSource(source);
        //todo:test
        recipeCommand.setImage(image);

        recipeCommand.setDirections(directions);
        recipeCommand.setDifficulty(difficulty);
        recipeCommand.setUrl(url);
        notes=new NotesCommand();
        notes.setId(new ObjectId().toString());
        notes.setDescription(descriptionNotes);
        recipeCommand.setNotes(notes);
        category=new CategoryCommand();
        category.setId(new ObjectId().toString());
        category.setDescription(descriptionCategory);
        recipeCommand.getCategories().add(category);
        ingredient1=new IngredientCommand();
        ingredient1.setId(new ObjectId().toString());
        ingredient2=new IngredientCommand();
        ingredient2.setId(new ObjectId().toString());
        recipeCommand.getIngredients().add(ingredient1);
        recipeCommand.getIngredients().add(ingredient2);
        Recipe recipe= recipeCommandToRecipeConverter.convert(recipeCommand);

        assertNotNull(recipe);
        assertEquals(recipeCommand.getId(),recipe.getId().toString());
        assertEquals(title,recipe.getTitle());
        assertEquals(prepTime,recipe.getPrepTime());
        assertEquals(cookTime,recipe.getCookTime());
        assertEquals(servings,recipe.getServings());
        assertEquals(source,recipe.getSource());
        assertEquals(image,recipe.getImage());
        assertEquals(directions,recipe.getDirections());
        assertEquals(url,recipe.getUrl());
        assertEquals(difficulty,recipe.getDifficulty());

        assertEquals(notes.getId(),recipe.getNotes().getId().toString());
        assertEquals(notes.getDescription(),recipe.getNotes().getDescription());


        assertNotNull(recipe.getNotes());
        assertNotNull(recipe.getCategories());
        assertNotNull(recipe.getIngredients());
        assertEquals(1,recipe.getCategories().size());
        assertEquals(2,recipe.getIngredients().size());

    }
}