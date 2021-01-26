package zatribune.spring.cookmaster.data.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@EqualsAndHashCode
@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String description;
    private BigDecimal amount;
    @OneToOne
    private UnitMeasure unitMeasure;

    @ManyToOne
    Recipe recipe;

    public Ingredient() {
    }

    public Ingredient(BigDecimal amount, UnitMeasure unitMeasure, String description){
        this.amount=amount;
        this.unitMeasure = unitMeasure;
        this.description=description;
    }
}
