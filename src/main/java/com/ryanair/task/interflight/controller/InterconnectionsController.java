package com.ryanair.task.interflight.controller;

import com.ryanair.task.interflight.model.Interconnection;
import com.ryanair.task.interflight.service.interfaces.DefaultInterconnectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class InterconnectionsController {

    static Logger log = Logger.getLogger(InterconnectionsController.class.getName());

    @Autowired
    private DefaultInterconnectionsService interconnectionsService;

    @GetMapping("/interconnections")
    public ResponseEntity<?> getInterconnections(@RequestParam String departure,
                                                 @RequestParam String arrival,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDateTime,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime arrivalDateTime) {

        log.info("Departure = " + departure +
                ", Arrival = " + arrival +
                ", Departure DATE_TIME = " + departureDateTime +
                ", Arrival DATE_TIME = " + arrivalDateTime);

        try {
            List<Interconnection> interconnections = interconnectionsService.findInterconnections(departure, arrival, departureDateTime, arrivalDateTime);
            if(interconnections.isEmpty()){
                return new ResponseEntity<>("There is no flight available for your research!", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(interconnections, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.BAD_REQUEST);
        }
    }

}
