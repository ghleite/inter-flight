package com.ryanair.task.interflight.service;

import com.ryanair.task.interflight.model.Route;
import com.ryanair.task.interflight.service.interfaces.DefaultRouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RouteService implements DefaultRouteService {

    private static final String ROUTES_URL = "https://services-api.ryanair.com/views/locate/3/routes";
    private static final String RYANAIR = "RYANAIR";

    private final RestTemplate restTemplate;

    public RouteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Route> getRyanairAndNoConnectingAirportRoutes() {
        ResponseEntity<Route[]> response = restTemplate.getForEntity(
                ROUTES_URL,
                Route[].class
        );
        
        List<Route> allRoutes = Arrays.asList(Objects.requireNonNull(response.getBody()));
        
        return allRoutes.stream()
                .filter(route -> RYANAIR.equalsIgnoreCase(route.getOperator()) &&
                        route.getConnectingAirport() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Route> findDirectAndIndirectRoutes(List<Route> routes, String departure, String arrival) {
        List<Route> directAndIndirectRoutes = new ArrayList<>();

        for (Route route : routes) {
            if (route.getAirportFrom().equals(departure) && route.getAirportTo().equals(arrival)) {
                directAndIndirectRoutes.add(route);
                break;
            }
        }

        for (Route route : routes) {
            if (route.getAirportFrom().equals(departure)) {
                String connectingAirport = route.getAirportTo();
                for (Route innerRoute : routes) {
                    if (innerRoute.getAirportFrom().equals(connectingAirport) && innerRoute.getAirportTo().equals(arrival)) {
                        directAndIndirectRoutes.add(route);
                        directAndIndirectRoutes.add(innerRoute);
                        break;
                    }
                }
            }
        }

        return directAndIndirectRoutes;
    }
}
