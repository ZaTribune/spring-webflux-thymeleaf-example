package zatribune.spring.kitchenmaster.converters;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zatribune.spring.kitchenmaster.commands.*;
import zatribune.spring.kitchenmaster.data.entities.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RecipeCommandToRecipeTest {
    private RecipeCommandToRecipe recipeCommandToRecipeConverter;
    final String title="a dummy recipe title";
    final Integer prepTime=10;
    final Integer cookTime=20;
    final Integer servings=5;
    final String source="a dummy recipe source";
    final String url="a dummy recipe url";
    final String directions="a dummy recipe directions";
    final Difficulty difficulty=Difficulty.MODERATE;
    final String image="a dummy recipe image";
    NotesCommand notes;
    final String descriptionNotes="a dummy notes description";
    CategoryCommand category;
    final String descriptionCategory="a dummy Category description";
    UnitMeasureCommand unitMeasureCommand1;
    UnitMeasureCommand unitMeasureCommand2;
    IngredientCommand ingredient1;
    IngredientCommand ingredient2;

    @BeforeEach
    void setUp() {
        recipeCommandToRecipeConverter =new RecipeCommandToRecipe(
                new NotesCommandToNotes()
                ,new CategoryCommandToCategory()
                ,new IngredientCommandToIngredient(new UnitMeasureCommandToUnitMeasure())
        );

        unitMeasureCommand1=new UnitMeasureCommand();
        unitMeasureCommand1.setId(new ObjectId().toString());
        unitMeasureCommand1.setDescription("desc1");
        unitMeasureCommand2=new UnitMeasureCommand();
        unitMeasureCommand2.setId(new ObjectId().toString());
        unitMeasureCommand2.setDescription("desc2");
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
        ingredient1.setUnitMeasure(unitMeasureCommand1);
        ingredient2=new IngredientCommand();
        ingredient2.setId(new ObjectId().toString());
        ingredient2.setUnitMeasure(unitMeasureCommand2);
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