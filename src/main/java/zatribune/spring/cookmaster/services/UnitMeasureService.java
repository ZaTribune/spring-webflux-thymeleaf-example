package zatribune.spring.cookmaster.services;

import reactor.core.publisher.Flux;
import zatribune.spring.cookmaster.commands.UnitMeasureCommand;
import zatribune.spring.cookmaster.data.entities.UnitMeasure;

public interface UnitMeasureService {
    Flux<UnitMeasureCommand> getAllUnitMeasures();
}
