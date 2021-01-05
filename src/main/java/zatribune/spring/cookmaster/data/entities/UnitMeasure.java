package zatribune.spring.cookmaster.data.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class UnitMeasure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;

}
