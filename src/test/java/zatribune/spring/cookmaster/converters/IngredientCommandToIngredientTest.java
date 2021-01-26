package zatribune.spring.cookmaster.converters;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zatribune.spring.cookmaster.commands.IngredientCommand;
import zatribune.spring.cookmaster.commands.UnitMeasureCommand;
import zatribune.spring.cookmaster.data.entities.Ingredient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientCommandToIngredientTest {

    private IngredientCommandToIngredient ingredientCommandToIngredientConverter;

    private IngredientCommand ingredientCommand;
    private final String descriptionIngredient ="a dummy ingredient description";
    private final BigDecimal amount=BigDecimal.valueOf(20);
    private UnitMeasureCommand unitMeasure;
    private final String idUnitMeasure="0x844454";
    private final String descriptionUnitMeasure="a dummy unit measure description";
    private final String idIngredient="0x899994";

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
    void testNullObject(){
        assertNull(ingredientCommandToIngredientConverter.convert(null));
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