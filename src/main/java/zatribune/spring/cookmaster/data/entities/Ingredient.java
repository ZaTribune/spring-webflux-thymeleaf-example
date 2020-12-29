package zatribune.spring.cookmaster.data.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
@Data
@EqualsAndHashCode(exclude = {"recipe"})
@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private BigDecimal amount;
    @OneToOne
    private UnitOfMeasure unitOfMeasure;

    @ManyToOne
    Recipe recipe;

    public Ingredient() {
    }

    public Ingredient(BigDecimal amount,UnitOfMeasure unitOfMeasure,String description){
        this.amount=amount;
        this.unitOfMeasure=unitOfMeasure;
        this.description=description;
    }
}
