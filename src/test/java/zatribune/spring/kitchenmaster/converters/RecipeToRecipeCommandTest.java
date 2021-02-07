package zatribune.spring.kitchenmaster.converters;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zatribune.spring.kitchenmaster.commands.RecipeCommand;
import zatribune.spring.kitchenmaster.data.entities.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RecipeToRecipeCommandTest {

    RecipeToRecipeCommand recipeToRecipeCommandConverter;
    final String title="a dummy recipe title";
    final Integer prepTime=10;
    final Integer cookTime=20;
    final Integer servings=5;
    final String source="a dummy recipe source";
    final String url="a dummy recipe url";
    final String directions="a dummy recipe directions";
    final Difficulty difficulty=Difficulty.MODERATE;
    final String image="a dummy recipe image";
    Notes notes;
    final String descriptionNotes="a dummy notes description";
    Category category;
    final String descriptionCategory="a dummy Category description";
    Ingredient ingredient1;
    Ingredient ingredient2;

    @BeforeEach
    void setUp() {
        recipeToRecipeCommandConverter =new RecipeToRecipeCommand(
                 new NotesToNotesCommand()
                ,new CategoryToCategoryCommand()
                ,new IngredientToIngredientCommand(new UnitMeasureToUnitMeasureCommand())
                );
    }

    @Test
    void testEmptyObject(){
        assertNotNull(recipeToRecipeCommandConverter.convert(new Recipe()));
    }


    @Test
    void convert() {
        Recipe recipe=new Recipe();
        recipe.setId(new ObjectId());
        recipe.setTitle(title);
        recipe.setCookTime(cookTime);
        recipe.setPrepTime(prepTime);
        recipe.setServings(servings);
        recipe.setSource(source);
        //todo:test
        recipe.setImage(image);

        recipe.setDirections(directions);
        recipe.setDifficulty(difficulty);
        recipe.setUrl(url);
        notes=new Notes();
        notes.setId(new ObjectId());
        notes.setDescription(descriptionNotes);
        recipe.setNotes(notes);
        category=new Category();
        category.setId(new ObjectId());
        category.setDescription(descriptionCategory);
        recipe.getCategories().add(category);
        ingredient1=new Ingredient();
        ingredient1.setId(new ObjectId());
        ingredient2=new Ingredient();
        ingredient2.setId(new ObjectId());
        recipe.addIngredient(ingredient1).addIngredient(ingredient2);

        RecipeCommand recipeCommand= recipeToRecipeCommandConverter.convert(recipe);

        assertNotNull(recipeCommand);
        assertEquals(recipe.getId().toString(),recipeCommand.getId());
        assertEquals(title,recipeCommand.getTitle());
        assertEquals(prepTime,recipeCommand.getPrepTime());
        assertEquals(cookTime,recipeCommand.getCookTime());
        assertEquals(servings,recipeCommand.getServings());
        assertEquals(source,recipeCommand.getSource());
        assertEquals(image,recipeCommand.getImage());
        assertEquals(directions,recipeCommand.getDirections());
        assertEquals(url,recipeCommand.getUrl());
        assertEquals(difficulty,recipeCommand.getDifficulty());

        assertEquals(notes.getId().toString(),recipeCommand.getNotes().getId());
        assertEquals(notes.getDescription(),recipeCommand.getNotes().getDescription());


        assertNotNull(recipeCommand.getNotes());
        assertNotNull(recipeCommand.getCategories());
        assertNotNull(recipeCommand.getIngredients());
        assertEquals(1,recipeCommand.getCategories().size());
        assertEquals(2,recipeCommand.getIngredients().size());

    }
}