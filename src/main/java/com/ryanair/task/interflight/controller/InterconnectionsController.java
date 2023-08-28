package com.ryanair.task.interflight.controller;

import com.ryanair.task.interflight.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class InterconnectionsController {

    static Logger log = Logger.getLogger(InterconnectionsController.class.getName());

    @GetMapping("/interconnections")
    public void getInterconnections(@RequestParam String departure,
                                    @RequestParam String arrival,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDateTime,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime arrivalDateTime) {

        // TODO - Request the service and treat errors

        log.info("Departure = " + departure +
                ", Arrival = " + arrival +
                ", Departure DATE_TIME = " + departureDateTime +
                ", Arrival DATE_TIME = " + arrivalDateTime);
    }

}
