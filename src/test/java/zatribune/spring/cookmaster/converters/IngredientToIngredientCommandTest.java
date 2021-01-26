package zatribune.spring.cookmaster.converters;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zatribune.spring.cookmaster.commands.IngredientCommand;
import zatribune.spring.cookmaster.data.entities.Ingredient;
import zatribune.spring.cookmaster.data.entities.UnitMeasure;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientToIngredientCommandTest {

    private IngredientToIngredientCommand ingredientToIngredientCommandConverter;

    private Ingredient ingredient;
    private final String descriptionIngredient ="a dummy ingredient description";
    private final BigDecimal amount=BigDecimal.valueOf(20);
    private UnitMeasure unitMeasure;
    private final String idUnitMeasure="0x844454";
    private final String descriptionUnitMeasure="a dummy unit measure description";
    private final String idIngredient="0x899994";

    @BeforeEach
    void setUp() {
        ingredientToIngredientCommandConverter =
                new IngredientToIngredientCommand(new UnitMeasureToUnitMeasureCommand());
    }

    @Test
    void testEmptyObject(){
        assertNotNull(ingredientToIngredientCommandConverter.convert(new Ingredient()));
    }

    @Test
    void testNullObject(){
        assertNull(ingredientToIngredientCommandConverter.convert(null));
    }

    @Test
    void convert() {
        ingredient =new Ingredient();
        ingredient.setId(new ObjectId(idIngredient));
        ingredient.setDescription(descriptionIngredient);
        ingredient.setAmount(amount);
        unitMeasure=new UnitMeasure();
        unitMeasure.setId(new ObjectId(idUnitMeasure));
        unitMeasure.setDescription(descriptionUnitMeasure);
        ingredient.setUnitMeasure(unitMeasure);

        IngredientCommand ingredientCommand= ingredientToIngredientCommandConverter.convert(ingredient);

        assertNotNull(ingredientCommand);
        assertEquals(idIngredient,ingredientCommand.getId());
        assertEquals(descriptionIngredient,ingredientCommand.getDescription());
        assertEquals(amount,ingredientCommand.getAmount());
        assertEquals(idUnitMeasure,ingredientCommand.getUnitMeasure().getId());
        assertEquals(descriptionUnitMeasure,ingredientCommand.getUnitMeasure().getDescription());



    }
}