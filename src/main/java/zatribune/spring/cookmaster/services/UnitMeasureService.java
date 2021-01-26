package zatribune.spring.cookmaster.services;

import zatribune.spring.cookmaster.data.entities.UnitMeasure;

import java.util.Optional;
import java.util.Set;

public interface UnitMeasureService {
    Optional<UnitMeasure>getUnitMeasureById(String id);
    UnitMeasure saveOrUpdate(UnitMeasure unitMeasure);
    void delete(UnitMeasure unitMeasure);
    void deleteById(String id);
    Set<UnitMeasure> getAllUnitMeasures();
}
