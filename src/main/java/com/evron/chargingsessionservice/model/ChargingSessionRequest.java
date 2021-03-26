package com.evron.chargingsessionservice.model;

import lombok.Getter;
import lombok.Setter;

/**
 * ChargingSessionRequest- contains station id of charging station.
 */
@Getter
@Setter
public class ChargingSessionRequest {
    private String stationId;
}
