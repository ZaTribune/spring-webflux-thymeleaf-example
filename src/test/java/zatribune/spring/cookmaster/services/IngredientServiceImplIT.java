package zatribune.spring.cookmaster.services;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import zatribune.spring.cookmaster.data.entities.Ingredient;
import zatribune.spring.cookmaster.data.entities.Recipe;
import zatribune.spring.cookmaster.exceptions.MyNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IngredientServiceImplIT {

    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
    }

    //these tests require the Ingredient Collection to be existed first
    //Because MongoDB creates a collection implicitly when
    // the collection is first referenced in a command
    /*@Test
    void getIngredientById() {
        Ingredient ing = recipeService.getAllRecipes()
                .iterator().next()
                .getIngredients()
                .iterator().next();
        System.out.println("mm" + ing.getId().toString());
        Ingredient ingredient = ingredientService.getIngredientById(ing.getId().toString());
        assertNotNull(ingredient);// = assertTrue(ingredient.isPresent());
        assertEquals(ing.getId(), ingredient.getId());

    }
    @Test
    void getIngredientsByRecipe() {
        //the first two has no ingredients
        Recipe recipe = recipeService.getAllRecipes()
                .iterator().next();
        Set<Ingredient> ingredientSet = ingredientService.getIngredientsByRecipe(recipe);
        assertFalse(ingredientSet.isEmpty());
    }

    @Test
    void deleteIngredient() {

    }

    @Test
    void deleteIngredientById() {
    }*/
}