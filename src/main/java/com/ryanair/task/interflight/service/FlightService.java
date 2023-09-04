package com.ryanair.task.interflight.service;

import com.ryanair.task.interflight.model.AvailableFlights;
import com.ryanair.task.interflight.service.interfaces.DefaultFlightService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FlightService implements DefaultFlightService {

    private static final String SCHEDULES_URL = "https://services-api.ryanair.com/timtbl/3/schedules";

    private final RestTemplate restTemplate;

    public FlightService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("availableFlights")
    @Override
    public AvailableFlights getAvailableFlights(String departureAirport,
                                                String arrivalAirport,
                                                Integer year,
                                                Integer month) {
        String apiUrl = SCHEDULES_URL + "/" + departureAirport + "/" + arrivalAirport +
                "/years/" + year + "/months/" + month;

        return restTemplate.getForObject(apiUrl, AvailableFlights.class);
    }
}
