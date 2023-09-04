package com.ryanair.task.interflight.service.interfaces;

import com.ryanair.task.interflight.model.AvailableFlights;

public interface DefaultFlightService {
    AvailableFlights getAvailableFlights(String departureAirport,
                                         String arrivalAirport,
                                         Integer year,
                                         Integer month);
}
