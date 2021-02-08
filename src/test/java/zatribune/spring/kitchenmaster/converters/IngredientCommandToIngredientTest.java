package zatribune.spring.kitchenmaster.converters;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zatribune.spring.kitchenmaster.commands.IngredientCommand;
import zatribune.spring.kitchenmaster.commands.UnitMeasureCommand;
import zatribune.spring.kitchenmaster.data.entities.Ingredient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientCommandToIngredientTest {

    private IngredientCommandToIngredient ingredientCommandToIngredientConverter;

    IngredientCommand ingredientCommand;
    String descriptionIngredient ="a dummy ingredient description";
    BigDecimal amount=BigDecimal.valueOf(20);
    UnitMeasureCommand unitMeasure;
    String idUnitMeasure="0x844454";
    String descriptionUnitMeasure="a dummy unit measure description";
    String idIngredient="0x899994";

    @BeforeEach
    void setUp() {
        ingredientCommandToIngredientConverter=
                new IngredientCommandToIngredient(new UnitMeasureCommandToUnitMeasure());
    }

    @Test
    void testEmptyObject(){
        assertNotNull(ingredientCommandToIngredientConverter.convert(new IngredientCommand()));
    }

    @Test
    void convert() {
        ingredientCommand =new IngredientCommand();
        ingredientCommand.setId(new ObjectId().toString());
        ingredientCommand.setDescription(descriptionIngredient);
        ingredientCommand.setAmount(amount);
        unitMeasure=new UnitMeasureCommand();
        unitMeasure.setId(new ObjectId().toString());
        unitMeasure.setDescription(descriptionUnitMeasure);
        ingredientCommand.setUnitMeasure(unitMeasure);

        Ingredient ingredient=ingredientCommandToIngredientConverter.convert(ingredientCommand);

        assertNotNull(ingredient);
        assertEquals(ingredientCommand.getId(),ingredient.getId().toString());
        assertEquals(descriptionIngredient,ingredient.getDescription());
        assertEquals(amount,ingredient.getAmount());
        assertEquals(unitMeasure.getId(),ingredient.getUnitMeasure().getId().toString());
        assertEquals(descriptionUnitMeasure,ingredient.getUnitMeasure().getDescription());
    }
}