package zatribune.spring.cookmaster.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import zatribune.spring.cookmaster.commands.NotesCommand;
import zatribune.spring.cookmaster.commands.RecipeCommand;
import zatribune.spring.cookmaster.data.entities.Difficulty;
import zatribune.spring.cookmaster.data.entities.Recipe;
import zatribune.spring.cookmaster.exceptions.MyNotFoundException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class RecipeServiceImplIT {


    @Autowired
    RecipeService recipeService;

    private final String title = "a dummy recipe title";
    private final Integer prepTime = 10;
    private final Integer cookTime = 20;
    private final Integer servings = 5;
    private final String source = "a dummy recipe source";
    private final String url = "a dummy recipe url";
    private final String directions = "a dummy recipe directions";
    private final Difficulty difficulty = Difficulty.MODERATE;
    private final String image = "a dummy recipe image";

    @Test
    void testGetAllRecipes() {
        Set<Recipe> recipeSet = recipeService.getAllRecipes();
        System.out.println(recipeSet.size());
    }

    @Test
    void testSaveRecipeCommand() {
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
        // it has no id , we expect it to have one after persisted on the database
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
        NotesCommand notes = new NotesCommand();
        notes.setDescription("arm");
        recipeCommand.setNotes(notes);

        // the save() method requires a working hibernate context for generating ids
        RecipeCommand returnedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);

        assertNotNull(returnedRecipeCommand);
        assertNotNull(returnedRecipeCommand.getId());
        assertEquals(title, returnedRecipeCommand.getTitle());
        assertEquals(prepTime, returnedRecipeCommand.getPrepTime());
        assertEquals(cookTime, returnedRecipeCommand.getCookTime());
        assertEquals(servings, returnedRecipeCommand.getServings());
        assertEquals(source, returnedRecipeCommand.getSource());
        assertEquals(image, returnedRecipeCommand.getImage());
        assertEquals(directions, returnedRecipeCommand.getDirections());
        assertEquals(url, returnedRecipeCommand.getUrl());
        assertEquals(difficulty, returnedRecipeCommand.getDifficulty());

        assertNotNull(returnedRecipeCommand.getNotes());
        assertEquals(notes.getDescription(), returnedRecipeCommand.getNotes().getDescription());


    }

    @Test
    void deleteRecipe() {
        //todo:check
        Recipe recipe = recipeService.getRecipeById(1L);
        assertNotNull(recipe);
        recipeService.deleteRecipe(recipe);
        Exception exception=assertThrows(MyNotFoundException.class,()->recipeService.getRecipeById(1L));
        // just an extra part for checking nothing more.
        assertTrue(exception.getMessage().contains("Recipe Not Found"));
    }

    @Test
    void deleteRecipeById() {
        Recipe recipe = recipeService.getRecipeById(1L);
        assertNotNull(recipe);
        recipeService.deleteRecipeById(1L);
        Exception exception=assertThrows(MyNotFoundException.class,()->recipeService.getRecipeById(1L));
        // just an extra part for checking nothing more.
        assertTrue(exception.getMessage().contains("Recipe Not Found"));
    }

}
