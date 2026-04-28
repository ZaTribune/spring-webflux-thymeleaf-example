package com.tribune.demo.km.data.entity;

import com.tribune.demo.km.config.ObjectIdHandler;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;


@Getter
@Setter
@Document
@NoArgsConstructor
public class Notes {

    @Id
    @JsonSerialize(using = ObjectIdHandler.Serializer.class)
    @JsonDeserialize(using = ObjectIdHandler.Deserializer.class)
    private ObjectId id;
    private String description;

    public Notes(String description) {
        //when inserted on the fly from a new recipe otherwise,it won't have an id
        this.id = new ObjectId();
        this.description = description;
    }
}
