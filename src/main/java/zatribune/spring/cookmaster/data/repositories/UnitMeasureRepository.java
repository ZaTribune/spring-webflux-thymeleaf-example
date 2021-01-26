package zatribune.spring.cookmaster.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zatribune.spring.cookmaster.data.entities.UnitMeasure;

import java.util.Optional;

public interface UnitMeasureRepository extends CrudRepository<UnitMeasure,String> {

    Optional<UnitMeasure>findUnitOfMeasureByDescription(String description);
}
