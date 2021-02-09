package zatribune.spring.kitchenmaster.converters;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zatribune.spring.kitchenmaster.commands.IngredientCommand;
import zatribune.spring.kitchenmaster.commands.UnitMeasureCommand;
import zatribune.spring.kitchenmaster.data.entities.Ingredient;
import zatribune.spring.kitchenmaster.data.entities.Recipe;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientCommandToIngredientTest {

    private IngredientCommandToIngredient ingredientCommandToIngredientConverter;

    IngredientCommand ingredientCommand;
    String descriptionIngredient ="a dummy ingredient description";
    BigDecimal amount=BigDecimal.valueOf(20);
    UnitMeasureCommand unitMeasure;
    ObjectId idUnitMeasure;
    String descriptionUnitMeasure="a dummy unit measure description";
    ObjectId idIngredient;

    @BeforeEach
    void setUp() {
        ingredientCommandToIngredientConverter=
                new IngredientCommandToIngredient(new UnitMeasureCommandToUnitMeasure());
        idUnitMeasure=new ObjectId();
        idIngredient=new ObjectId();
    }

    @Test
    void testEmptyObject(){
        assertNotNull(ingredientCommandToIngredientConverter.convert(new IngredientCommand()));
    }

    @Test
    void convert() {
        ingredientCommand =new IngredientCommand();
        ingredientCommand.setId(idIngredient.toString());
        ingredientCommand.setDescription(descriptionIngredient);
        ingredientCommand.setAmount(amount);
        unitMeasure=new UnitMeasureCommand();
        unitMeasure.setId(idUnitMeasure.toString());
        unitMeasure.setDescription(descriptionUnitMeasure);
        ingredientCommand.setUnitMeasure(unitMeasure);
        ingredientCommand.setRecipe(new Recipe());

        Ingredient ingredient=ingredientCommandToIngredientConverter.convert(ingredientCommand);

        assertNotNull(ingredient);
        assertEquals(ingredientCommand.getId(),ingredient.getId().toString());
        assertEquals(descriptionIngredient,ingredient.getDescription());
        assertEquals(amount,ingredient.getAmount());
        assertEquals(unitMeasure.getId(),ingredient.getUnitMeasure().getId().toString());
        assertEquals(descriptionUnitMeasure,ingredient.getUnitMeasure().getDescription());
    }
}