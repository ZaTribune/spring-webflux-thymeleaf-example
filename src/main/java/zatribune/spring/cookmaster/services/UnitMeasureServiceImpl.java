package zatribune.spring.cookmaster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import zatribune.spring.cookmaster.commands.UnitMeasureCommand;
import zatribune.spring.cookmaster.converters.UnitMeasureToUnitMeasureCommand;
import zatribune.spring.cookmaster.data.repositories.UnitMeasureReactiveRepository;

@Service
public class UnitMeasureServiceImpl implements UnitMeasureService {

    private final UnitMeasureReactiveRepository repository;
    private final UnitMeasureToUnitMeasureCommand converter;

    @Autowired
    public UnitMeasureServiceImpl(UnitMeasureReactiveRepository repository,UnitMeasureToUnitMeasureCommand converter) {
        this.repository = repository;
        this.converter=converter;
    }

    @Override
    public Flux<UnitMeasureCommand> getAllUnitMeasures() {
        return repository.findAll().map(converter::convert);
    }
}
