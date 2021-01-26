package zatribune.spring.cookmaster.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zatribune.spring.cookmaster.data.entities.Recipe;

import java.math.BigDecimal;
@Setter
@Getter
@NoArgsConstructor
public class IngredientCommand {
    String id;
    String description;
    BigDecimal amount;
    UnitMeasureCommand unitMeasure;
    Recipe recipe;
}
