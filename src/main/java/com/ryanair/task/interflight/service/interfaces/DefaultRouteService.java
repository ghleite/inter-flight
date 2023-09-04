package com.ryanair.task.interflight.service.interfaces;

import com.ryanair.task.interflight.model.Route;

import java.util.List;

public interface DefaultRouteService {

    List<Route> getRyanairAndNoConnectingAirportRoutes();

    List<Route> findDirectAndIndirectRoutes(List<Route> routes, String departure, String arrival);
}
