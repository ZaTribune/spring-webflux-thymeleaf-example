package zatribune.spring.cookmaster.data.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class UnitMeasure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String description;

    public UnitMeasure(String description) {
        this.description=description;
    }
}
