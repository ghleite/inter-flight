package com.ryanair.task.interflight.service;

import com.ryanair.task.interflight.model.*;
import com.ryanair.task.interflight.service.interfaces.DefaultFlightService;
import com.ryanair.task.interflight.service.interfaces.DefaultInterconnectionsService;
import com.ryanair.task.interflight.service.interfaces.DefaultRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

@Service
public class InterconnectionsService implements DefaultInterconnectionsService {

    private static final int ZERO_STOPS = 0;
    static Logger log = Logger.getLogger(InterconnectionsService.class.getName());

    @Autowired
    private DefaultFlightService flightService;

    @Autowired
    private DefaultRouteService routeService;

    public InterconnectionsService(FlightService flightService) {
        this.flightService = flightService;
    }

    @Override
    public List<Interconnection> findInterconnections(String departure,
                                                      String arrival,
                                                      LocalDateTime departureDateTime,
                                                      LocalDateTime arrivalDateTime) {
        List<Route> routes = this.routeService.getRyanairAndNoConnectingAirportRoutes();
        List<Route> directAndIndirectRoutes = this.routeService.findDirectAndIndirectRoutes(routes, departure, arrival);

        log.info("All Ryanair routes size : " + routes.size());
        log.info("Direct and indirect routes size : " + directAndIndirectRoutes.size());

        return findAllFlights(directAndIndirectRoutes,
                departure, arrival, departureDateTime, arrivalDateTime);
    }

    private List<Interconnection> findAllFlights(List<Route> directAndIndirectRoutes,
                                                 String departure,
                                                 String arrival,
                                                 LocalDateTime departureDateTime,
                                                 LocalDateTime arrivalDateTime) {
        List<Interconnection> interconnectionList = new ArrayList<>();

        for (Route route : directAndIndirectRoutes) {
            if (this.isDirectRoute(route, departure, arrival)) {
                interconnectionList.addAll(Objects
                        .requireNonNull(this.buildDirectFlights(route, departureDateTime, arrivalDateTime)));
            } else {
                // TODO - function to build indirect flights
            }
        }

        return interconnectionList;
    }

    private List<Interconnection> buildDirectFlights(Route route, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        List<Interconnection> directFlightsList = new ArrayList<>();

        final int departureYear = departureDateTime.getYear();
        final int departureMonth = departureDateTime.getMonthValue();

        final int arrivalYear = arrivalDateTime.getYear();
        final int arrivalMonth = arrivalDateTime.getMonthValue();

        AvailableFlights availableFlights =
                this.flightService.getAvailableFlights(route.getAirportFrom(),
                        route.getAirportTo(),
                        departureDateTime.getYear(),
                        departureDateTime.getMonthValue());

        AvailableFlights filteredAvailableFlights = new AvailableFlights();
        filteredAvailableFlights.setMonth(availableFlights.getMonth());
        filteredAvailableFlights.setDays(new ArrayList<>());
        filteredAvailableFlights.getDays().addAll(
                availableFlights.getDays().stream()
                        .filter(
                                dayFlights -> dayFlights.getDay() >= departureDateTime.getDayOfMonth() &&
                                        dayFlights.getDay() <= arrivalDateTime.getDayOfMonth()
                        )
                        .toList());

        for (DayFlights dayFlights : filteredAvailableFlights.getDays()) {
            for (Flight flight : dayFlights.getFlights()) {
                if (flight.getDepartureDateTime(departureYear, departureMonth, dayFlights.getDay()).isAfter(departureDateTime) &&
                        flight.getArrivalDateTime(arrivalYear, arrivalMonth, dayFlights.getDay()).isBefore(arrivalDateTime))
                    directFlightsList.add(
                            buildInterconnectionObject(ZERO_STOPS, route.getAirportFrom(), route.getAirportTo(),
                                    flight.getDepartureDateTime(departureYear, departureMonth, dayFlights.getDay()),
                                    flight.getArrivalDateTime(arrivalYear, arrivalMonth, dayFlights.getDay()))
                    );
            }
        }

        return directFlightsList;
    }

    private Interconnection buildInterconnectionObject(int stops, String airportFrom, String airportTo,
                                                       LocalDateTime departureTime, LocalDateTime arrivalTime) {
        Interconnection interconnection = new Interconnection();
        interconnection.setStops(stops);

        Leg leg = new Leg.Builder()
                .withDepartureAirport(airportFrom)
                .withArrivalAirport(airportTo)
                .withDepartureDateTime(departureTime)
                .withArrivalDateTime(arrivalTime)
                .build();
        interconnection.setLegs(Collections.singletonList(leg));

        return interconnection;
    }

    private boolean isDirectRoute(Route route, String departure, String arrival) {
        return departure.equals(route.getAirportFrom()) && arrival.equals(route.getAirportTo());
    }
}
