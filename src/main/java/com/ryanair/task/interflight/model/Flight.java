package com.ryanair.task.interflight.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Flight {
    private String carrierCode;
    private String number;
    private String departureTime;
    private String arrivalTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flight that)) return false;
        return Objects.equals(carrierCode, that.carrierCode) && Objects.equals(number, that.number) && Objects.equals(departureTime, that.departureTime) && Objects.equals(arrivalTime, that.arrivalTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carrierCode, number, departureTime, arrivalTime);
    }
}
