package com.ryanair.task.interflight.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Leg {
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;


    public static class Builder {
        private final Leg legToBuild;

        public Builder() {
            legToBuild = new Leg();
        }

        public Builder withDepartureAirport(String departureAirport) {
            legToBuild.departureAirport = departureAirport;
            return this;
        }

        public Builder withArrivalAirport(String arrivalAirport) {
            legToBuild.arrivalAirport = arrivalAirport;
            return this;
        }

        public Builder withDepartureDateTime(LocalDateTime departureDateTime) {
            legToBuild.departureDateTime = departureDateTime;
            return this;
        }

        public Builder withArrivalDateTime(LocalDateTime arrivalDateTime) {
            legToBuild.arrivalDateTime = arrivalDateTime;
            return this;
        }

        public Leg build() {
            if (legToBuild.departureAirport == null || legToBuild.arrivalAirport == null ||
                    legToBuild.departureDateTime == null || legToBuild.arrivalDateTime == null) {
                throw new IllegalStateException("Missing required fields in Leg object.");
            }
            return legToBuild;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Leg leg)) return false;
        return Objects.equals(departureAirport, leg.departureAirport) && Objects.equals(arrivalAirport, leg.arrivalAirport) && Objects.equals(departureDateTime, leg.departureDateTime) && Objects.equals(arrivalDateTime, leg.arrivalDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departureAirport, arrivalAirport, departureDateTime, arrivalDateTime);
    }
}
