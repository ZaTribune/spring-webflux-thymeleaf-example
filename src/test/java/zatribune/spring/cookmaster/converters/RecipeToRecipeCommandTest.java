package zatribune.spring.cookmaster.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zatribune.spring.cookmaster.commands.RecipeCommand;
import zatribune.spring.cookmaster.data.entities.*;

import static org.junit.jupiter.api.Assertions.*;

class RecipeToRecipeCommandTest {

    private RecipeToRecipeCommand recipeToRecipeCommandConverter;
    private final Long idRecipe=14L;
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
    private final Long idNotes=66L;
    private Category category;
    private final Long idCategory=33L;
    private final String descriptionCategory="a dummy Category description";
    private Ingredient ingredient1;
    private Ingredient ingredient2;
    private final Long idIngredient1=77L;
    private final Long idIngredient2=78L;

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
        recipe.setId(idRecipe);
        recipe.setTitle(title);
        recipe.setCookTime(cookTime);
        recipe.setPrepTime(prepTime);
        recipe.setServings(servings);
        recipe.setSource(source);
        recipe.setImage(image);
        recipe.setDirections(directions);
        recipe.setDifficulty(difficulty);
        recipe.setUrl(url);
        notes=new Notes();
        notes.setId(idNotes);
        notes.setDescription(descriptionNotes);
        recipe.setNotes(notes);
        category=new Category();
        category.setId(idCategory);
        category.setDescription(descriptionCategory);
        recipe.getCategories().add(category);
        ingredient1=new Ingredient();
        ingredient1.setId(idIngredient1);
        ingredient2=new Ingredient();
        ingredient2.setId(idIngredient2);
        recipe.addIngredient(ingredient1).addIngredient(ingredient2);

        RecipeCommand recipeCommand= recipeToRecipeCommandConverter.convert(recipe);

        assertNotNull(recipeCommand);
        assertEquals(idRecipe,recipeCommand.getId());
        assertEquals(title,recipeCommand.getTitle());
        assertEquals(prepTime,recipeCommand.getPrepTime());
        assertEquals(cookTime,recipeCommand.getCookTime());
        assertEquals(servings,recipeCommand.getServings());
        assertEquals(source,recipeCommand.getSource());
        assertEquals(image,recipeCommand.getImage());
        assertEquals(directions,recipeCommand.getDirections());
        assertEquals(url,recipeCommand.getUrl());
        assertEquals(difficulty,recipeCommand.getDifficulty());

        assertEquals(notes.getId(),recipeCommand.getNotes().getId());
        assertEquals(notes.getDescription(),recipeCommand.getNotes().getDescription());


        assertNotNull(recipeCommand.getNotes());
        assertNotNull(recipeCommand.getCategories());
        assertNotNull(recipeCommand.getIngredients());
        assertEquals(1,recipeCommand.getCategories().size());
        assertEquals(2,recipeCommand.getIngredients().size());

    }
}