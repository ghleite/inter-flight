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
public class DayFlights {
    private Integer day;
    private List<Flight> flights;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DayFlights that)) return false;
        return Objects.equals(day, that.day) && Objects.equals(flights, that.flights);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, flights);
    }
}
