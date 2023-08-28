package com.ryanair.task.interflight.service;

import com.ryanair.task.interflight.model.AvailableFlights;
import com.ryanair.task.interflight.model.Route;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class FlightService {

    private static final String ROUTES_URL = "https://services-api.ryanair.com/views/locate/3/routes";
    private static final String SCHEDULES_URL = "https://services-api.ryanair.com/timtbl/3/schedules";
    private final RestTemplate restTemplate;

    public FlightService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Route> getRoutes() {
        ResponseEntity<Route[]> response = restTemplate.getForEntity(
                ROUTES_URL,
                Route[].class
        );

        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

    public AvailableFlights getAvailableFlights(String departureAirport,
                                                      String arrivalAirport,
                                                      String year,
                                                      String month) {
        String apiUrl = SCHEDULES_URL + "/" + departureAirport + "/" + arrivalAirport +
                "/years/" + year + "/months/" + month;

        return restTemplate.getForObject(apiUrl, AvailableFlights.class);
    }
}
