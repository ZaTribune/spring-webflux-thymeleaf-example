package zatribune.spring.kitchenmaster.data.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import zatribune.spring.kitchenmaster.data.entities.Ingredient;
import zatribune.spring.kitchenmaster.data.entities.Recipe;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
class IngredientReactiveRepositoryIT {

    @Autowired
    IngredientReactiveRepository ingredientRepository;
    @Autowired
    RecipeReactiveRepository recipeRepository;

    String recipeTitle = "Hamburger";
    String ing1description = "fresh tomatoes";
    String ing2description = "juicy lemon";
    String ing3description = "salt";

    @BeforeEach
    void setUp() {
        ingredientRepository.deleteAll().block();
    }

    @Test
    void save() {
        Ingredient ingredient = new Ingredient();
        ingredient.setDescription(ing1description);
        ingredientRepository.save(ingredient).block();
        assertEquals(1L, ingredientRepository.count().block());
    }

    @Test
    void findByDescription() {
        Ingredient ingredient = new Ingredient();
        ingredient.setDescription(ing1description);
        ingredientRepository.save(ingredient).block();
        Ingredient returnedIngredient = ingredientRepository.findByDescription(ing1description).block();
        assertNotNull(returnedIngredient);
        assertEquals(ing1description, returnedIngredient.getDescription());
    }

    @Test
    void saveAllAndFindAll() {
        Recipe recipe = new Recipe();
        recipe.setTitle(recipeTitle);
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setDescription(ing1description);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setDescription(ing2description);
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setDescription(ing3description);
        /*
        Note that the mapping framework doesn't handle cascading operations.
         So – for instance – if we trigger a save on a parent, the child won't be saved automatically
          – we'll need to explicitly trigger the save on the child if we want to save it as well.
          */
        //subscribe ==[similar to]== forEach
        ingredientRepository.saveAll(Arrays.asList(ingredient1, ingredient2, ingredient3))
                .subscribe(ingredient ->System.out.println("" + ingredient.getId() + "\t" + ingredient.getDescription()));
        assertEquals(3L, ingredientRepository.count().block());
    }
}