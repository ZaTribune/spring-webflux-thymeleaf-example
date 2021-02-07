package zatribune.spring.kitchenmaster.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import zatribune.spring.kitchenmaster.services.UnitMeasureService;

@Controller
public class UnitMeasureController {

    private final UnitMeasureService service;

    @Autowired
    public UnitMeasureController(UnitMeasureService service) {
        this.service = service;
    }



}
