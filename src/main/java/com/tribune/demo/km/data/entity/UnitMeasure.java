package com.tribune.demo.km.data.entity;

import com.tribune.demo.km.config.ObjectIdHandler;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;


@Getter
@Setter
@NoArgsConstructor
@Document
public class UnitMeasure {

    @Id
    @JsonSerialize(using = ObjectIdHandler.Serializer.class)
    @JsonDeserialize(using = ObjectIdHandler.Deserializer.class)
    private ObjectId id;
    private String description;

}
