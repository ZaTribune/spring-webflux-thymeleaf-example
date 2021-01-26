package zatribune.spring.cookmaster.data.entities;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"recipes"})
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String description;

    private String info;

    @Lob
    private Byte[] image;

    @ManyToMany(mappedBy = "categories")// the name on the other side of the relationship
    private Set<Recipe>recipes=new HashSet<>();
}
