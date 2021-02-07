package zatribune.spring.kitchenmaster.services;

import reactor.core.publisher.Flux;
import zatribune.spring.kitchenmaster.commands.UnitMeasureCommand;

public interface UnitMeasureService {
    Flux<UnitMeasureCommand> getAllUnitMeasures();
}
