package zatribune.spring.cookmaster.data.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@NoArgsConstructor
@Document
public class UnitMeasure {
    @Id
    private ObjectId id;
    private String description;

    public UnitMeasure(String description) {
        //when inserted on the fly from a new recipe otherwise,it won't have an id
        this.id=new ObjectId();
        this.description=description;
    }
}
