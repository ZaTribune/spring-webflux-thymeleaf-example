package com.tribune.demo.km.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import com.tribune.demo.km.command.UnitMeasureCommand;
import com.tribune.demo.km.converter.UnitMeasureToUnitMeasureCommand;
import com.tribune.demo.km.data.repository.UnitMeasureReactiveRepository;

@Service
public record UnitMeasureServiceImpl(UnitMeasureReactiveRepository repository,
                                    UnitMeasureToUnitMeasureCommand converter) implements UnitMeasureService {


    @Override
    public Flux<UnitMeasureCommand> getAllUnitMeasures() {
        return repository.findAll().map(converter::convert);
    }
}
