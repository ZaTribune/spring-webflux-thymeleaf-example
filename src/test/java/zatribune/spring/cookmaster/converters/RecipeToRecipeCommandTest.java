package zatribune.spring.cookmaster.converters;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zatribune.spring.cookmaster.commands.CategoryCommand;
import zatribune.spring.cookmaster.commands.IngredientCommand;
import zatribune.spring.cookmaster.commands.NotesCommand;
import zatribune.spring.cookmaster.commands.RecipeCommand;
import zatribune.spring.cookmaster.data.entities.*;

import static org.junit.jupiter.api.Assertions.*;

class RecipeToRecipeCommandTest {

    private RecipeToRecipeCommand recipeToRecipeCommandConverter;
    private final String title="a dummy recipe title";
    private final Integer prepTime=10;
    private final Integer cookTime=20;
    private final Integer servings=5;
    private final String source="a dummy recipe source";
    private final String url="a dummy recipe url";
    private final String directions="a dummy recipe directions";
    private final Difficulty difficulty=Difficulty.MODERATE;
    private final String image="a dummy recipe image";
    private Notes notes;
    private final String descriptionNotes="a dummy notes description";
    private Category category;
    private final String descriptionCategory="a dummy Category description";
    private Ingredient ingredient1;
    private Ingredient ingredient2;

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
    void testNullObject(){
        assertNull(recipeToRecipeCommandConverter.convert(null));
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