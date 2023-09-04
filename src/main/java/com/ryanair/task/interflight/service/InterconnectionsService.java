package com.ryanair.task.interflight.service;

import com.ryanair.task.interflight.model.*;
import com.ryanair.task.interflight.service.interfaces.DefaultFlightService;
import com.ryanair.task.interflight.service.interfaces.DefaultInterconnectionsService;
import com.ryanair.task.interflight.service.interfaces.DefaultRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class InterconnectionsService implements DefaultInterconnectionsService {

    private static final int ZERO_STOPS = 0;

    private static final int ONE_STOPS = 1;
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

        for (int i = 0; i < directAndIndirectRoutes.size(); i++) {
            if (this.isDirectRoute(directAndIndirectRoutes.get(i), departure, arrival)) {
                interconnectionList.addAll(Objects
                        .requireNonNull(this.buildDirectFlights(directAndIndirectRoutes.get(i), departureDateTime, arrivalDateTime)));
            } else {
                if (departure.equals(directAndIndirectRoutes.get(i).getAirportFrom()))
                    interconnectionList.addAll(Objects
                            .requireNonNull(this.buildInterconnectedFlights(directAndIndirectRoutes.get(i),
                                    directAndIndirectRoutes.get(i + 1), departureDateTime, arrivalDateTime)));
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
                        flight.getArrivalDateTime(arrivalYear, arrivalMonth, dayFlights.getDay()).isBefore(arrivalDateTime)) {
                    Leg leg = buildLegObject(route.getAirportFrom(), route.getAirportTo(),
                            flight.getDepartureDateTime(departureYear, departureMonth, dayFlights.getDay()),
                            flight.getArrivalDateTime(arrivalYear, arrivalMonth, dayFlights.getDay()));
                    directFlightsList.add(
                            buildInterconnectionObject(ZERO_STOPS, Collections.singletonList(leg))
                    );
                }
            }
        }

        return directFlightsList;
    }

    private List<Interconnection> buildInterconnectedFlights(Route routeDeparture, Route routeArrival,
                                                             LocalDateTime departureDateTime,
                                                             LocalDateTime arrivalDateTime) {
        List<Interconnection> interconnectedFlightsList = new ArrayList<>();

        List<Interconnection> firstFlights = buildDirectFlights(routeDeparture, departureDateTime, arrivalDateTime);
        List<Interconnection> secondFlights = buildDirectFlights(routeArrival, departureDateTime, arrivalDateTime);

        if (firstFlights.isEmpty() || secondFlights.isEmpty())
            return interconnectedFlightsList;

        for (Interconnection firstFlight : firstFlights) {
            for (Interconnection secondFlight : secondFlights) {
                if (secondFlight.getLegs().get(0).getDepartureDateTime().isAfter(
                        firstFlight.getLegs().get(0).getArrivalDateTime().plusHours(2)
                )) {
                    interconnectedFlightsList.add(buildInterconnectionObject(firstFlight, secondFlight));
                }
            }
        }

        return interconnectedFlightsList;
    }

    private Interconnection buildInterconnectionObject(int stops, List<Leg> legs) {
        Interconnection interconnection = new Interconnection();
        interconnection.setStops(stops);
        interconnection.setLegs(legs);

        return interconnection;
    }

    private Interconnection buildInterconnectionObject(Interconnection firstFlight, Interconnection secondFlight) {
        List<Leg> legs = new ArrayList<>();
        Leg firstFlightLeg = firstFlight.getLegs().get(0);
        Leg secondFlightLeg = secondFlight.getLegs().get(0);

        legs.add(buildLegObject(firstFlightLeg.getDepartureAirport(), firstFlightLeg.getArrivalAirport(),
                firstFlightLeg.getDepartureDateTime(), firstFlightLeg.getArrivalDateTime()));
        legs.add(buildLegObject(secondFlightLeg.getDepartureAirport(), secondFlightLeg.getArrivalAirport(),
                secondFlightLeg.getDepartureDateTime(), secondFlightLeg.getArrivalDateTime()));

        return buildInterconnectionObject(ONE_STOPS, legs);
    }

    public Leg buildLegObject(String airportFrom, String airportTo,
                              LocalDateTime departureTime, LocalDateTime arrivalTime) {
        return new Leg.Builder()
                .withDepartureAirport(airportFrom)
                .withArrivalAirport(airportTo)
                .withDepartureDateTime(departureTime)
                .withArrivalDateTime(arrivalTime)
                .build();
    }

    private boolean isDirectRoute(Route route, String departure, String arrival) {
        return departure.equals(route.getAirportFrom()) && arrival.equals(route.getAirportTo());
    }
}
