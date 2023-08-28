package com.ryanair.task.interflight.service;

import com.ryanair.task.interflight.model.AvailableFlights;
import com.ryanair.task.interflight.model.Interconnection;
import com.ryanair.task.interflight.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class InterconnectionsService {

    static Logger log = Logger.getLogger(InterconnectionsService.class.getName());

    @Autowired
    private final FlightService flightService;

    public InterconnectionsService(FlightService flightService) {
        this.flightService = flightService;
    }

    public List<Interconnection> findInterconnections(String departure,
                                                      String arrival,
                                                      LocalDateTime departureDateTime,
                                                      LocalDateTime arrivalDateTime) {
        List<Route> routes = this.flightService.getRoutes();
        AvailableFlights availableFlights = this.flightService.getAvailableFlights("DUB", "WRO", "2023", "8");

        // TODO - Business logic to filtered and deliver the date about connections

        log.info(availableFlights.toString());
        log.info(routes.toString());

        return new ArrayList<>();
    }
}
