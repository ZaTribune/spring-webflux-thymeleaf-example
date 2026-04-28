package com.tribune.demo.km.command;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.tribune.demo.km.data.entity.Recipe;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
@Setter
@Getter
@NoArgsConstructor
public class IngredientCommand {
    String id;
    @NotBlank
    @Size(min = 3,max = 100)
    String description;
    @Min(0)
    @Max(50)
    BigDecimal amount;
    @NotNull
    UnitMeasureCommand unitMeasure;
    Recipe recipe;
}
