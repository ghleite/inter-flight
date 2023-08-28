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
public class Interconnection {
    private Integer stops;
    private List<Leg> legs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Interconnection that)) return false;
        return Objects.equals(stops, that.stops) && Objects.equals(legs, that.legs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stops, legs);
    }
}
