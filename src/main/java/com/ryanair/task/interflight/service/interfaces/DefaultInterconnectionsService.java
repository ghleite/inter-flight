package com.ryanair.task.interflight.service.interfaces;

import com.ryanair.task.interflight.model.Interconnection;

import java.time.LocalDateTime;
import java.util.List;


public interface DefaultInterconnectionsService {

    List<Interconnection> findInterconnections(String departure,
                                               String arrival,
                                               LocalDateTime departureDateTime,
                                               LocalDateTime arrivalDateTime);
}
