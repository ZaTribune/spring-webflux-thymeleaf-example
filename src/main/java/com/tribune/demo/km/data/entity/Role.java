package com.tribune.demo.km.data.entity;

import com.tribune.demo.km.config.ObjectIdHandler;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

@Getter
@Setter
@Document
public class Role implements GrantedAuthority {

    @Id
    @JsonSerialize(using = ObjectIdHandler.Serializer.class)
    @JsonDeserialize(using = ObjectIdHandler.Deserializer.class)
    private ObjectId id;

    private String name;

    @Override
    public String getAuthority() {
        return name;
    }

}
