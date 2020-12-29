package zatribune.spring.cookmaster.data.entities;

import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(exclude = {"recipe"})
@Entity
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Recipe recipe;
    @Lob
    private String description;

    public Notes() {
    }

    public Notes(String description) {
        this.description=description;
    }
}
