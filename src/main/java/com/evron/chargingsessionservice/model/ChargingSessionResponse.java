package com.evron.chargingsessionservice.model;

import com.evron.chargingsessionservice.service.ChargingSessionService;

import java.util.List;

import lombok.Data;

/**
 * Model class for response of service {@link ChargingSessionService}.
 * Serialized JSON would look like:
 *
 * <pre>
 * {
 * "chargingSessions": [
 * {
 * "id": "2649d05c-2b82-4c9e-8460-782891f665c8",
 * "stationId": "MNO-12345",
 * "startedAt": "2021-03-15T14:44:24.2349397",
 * "status": "IN_PROGRESS"
 * }
 * ]
 * }
 * </pre>
 */
@Data
public class ChargingSessionResponse {

    private List<ChargingSession> chargingSessions;
}
