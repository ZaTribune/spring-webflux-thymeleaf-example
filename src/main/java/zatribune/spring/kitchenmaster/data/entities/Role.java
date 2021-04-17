package zatribune.spring.kitchenmaster.data.entities;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@Document
public class Role implements GrantedAuthority {
    @Id
    private ObjectId id;

    private String name;

    @Override
    public String getAuthority() {
        return name;
    }

}
