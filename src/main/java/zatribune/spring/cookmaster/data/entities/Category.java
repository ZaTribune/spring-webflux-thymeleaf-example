package zatribune.spring.cookmaster.data.entities;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Document
public class Category {
    @Id
    private ObjectId id;

    private String description;

    private String info;

    private Byte[] image;
    @DBRef
    private Set<Recipe>recipes=new HashSet<>();
}
