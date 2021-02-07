package zatribune.spring.kitchenmaster.data.entities;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document
@NoArgsConstructor
public class Notes {
    @Id
    private ObjectId id;
    @DBRef
    private Recipe recipe;
    private String description;

    public Notes(String description) {
        //when inserted on the fly from a new recipe otherwise,it won't have an id
        this.id=new ObjectId();
        this.description=description;
    }
}
