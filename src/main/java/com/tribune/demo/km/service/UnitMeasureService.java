package com.tribune.demo.km.service;

import reactor.core.publisher.Flux;
import com.tribune.demo.km.command.UnitMeasureCommand;

public interface UnitMeasureService {
    Flux<UnitMeasureCommand> getAllUnitMeasures();
}
