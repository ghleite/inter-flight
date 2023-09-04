package com.ryanair.task.interflight.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Route {
    private String airportFrom;
    private String airportTo;
    private String connectingAirport;
    private boolean newRoute;
    private boolean seasonalRoute;
    private String operator;
    private String group;
    private List<String> similarArrivalAirportCodes;
    private List<String> tags;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route route)) return false;
        return newRoute == route.newRoute && seasonalRoute == route.seasonalRoute && Objects.equals(airportFrom, route.airportFrom) && Objects.equals(airportTo, route.airportTo) && Objects.equals(connectingAirport, route.connectingAirport) && Objects.equals(operator, route.operator) && Objects.equals(group, route.group) && Objects.equals(similarArrivalAirportCodes, route.similarArrivalAirportCodes) && Objects.equals(tags, route.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airportFrom, airportTo, connectingAirport, newRoute, seasonalRoute, operator, group, similarArrivalAirportCodes, tags);
    }
}


