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
public class AvailableFlights {
    private int month;
    private List<DayFlights> days;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvailableFlights that)) return false;
        return Objects.equals(month, that.month) && Objects.equals(days, that.days);
    }

    @Override
    public int hashCode() {
        return Objects.hash(month, days);
    }
}
