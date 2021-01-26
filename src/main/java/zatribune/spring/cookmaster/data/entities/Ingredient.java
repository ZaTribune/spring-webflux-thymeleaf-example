package zatribune.spring.cookmaster.data.entities;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;


@Getter
@Setter
@Document
public class Ingredient {
    @Id
    private ObjectId id;
    private String description;
    private BigDecimal amount;
    @DBRef
    private UnitMeasure unitMeasure;
    @DBRef
    private Recipe recipe;

    public Ingredient() {
    }

    public Ingredient(BigDecimal amount, UnitMeasure unitMeasure, String description){
        //when inserted on the fly from a new recipe otherwise,it won't have an id
        this.id=new ObjectId();
        this.amount=amount;
        this.unitMeasure = unitMeasure;
        this.description=description;
    }
}
