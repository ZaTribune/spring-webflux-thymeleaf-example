package zatribune.spring.cookmaster.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import zatribune.spring.cookmaster.data.entities.Ingredient;
import zatribune.spring.cookmaster.data.entities.Recipe;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IngredientServiceImplIT {

    @Autowired
    private IngredientService ingredientService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getIngredientById() {
        String id="0x456456";
        Optional<Ingredient>ingredient=ingredientService.getIngredientById(id);
        assertFalse(ingredient.isEmpty());// = assertTrue(ingredient.isPresent());
        ingredient.ifPresent(ing->assertEquals(id,ingredient.get().getId()));

    }

    @Test
    void getIngredientsByRecipe() {
        //the first two has no ingredients
        String recipeId="0x456456";
        Set<Ingredient>ingredientSet=ingredientService.getIngredientsByRecipe(new Recipe(recipeId));
        assertFalse(ingredientSet.isEmpty());
    }

    @Test
    void deleteIngredient() {

    }

    @Test
    void deleteIngredientById() {
    }
}