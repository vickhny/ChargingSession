package com.evron.chargingsessionservice.service;

import com.evron.chargingsessionservice.exception.InvalidInputParameterException;
import com.evron.chargingsessionservice.model.ChargingSession;
import com.evron.chargingsessionservice.model.ChargingSessionRequest;
import com.evron.chargingsessionservice.model.ChargingSessionResponse;
import com.evron.chargingsessionservice.model.MetricsCount;

import java.util.UUID;

/**
 * ChargingSession service interface. It contains method to manipulate charging
 * session data.
 */
public interface ChargingSessionService {

    /**
     * Adds new ChargingSession to the in-memory data structures.
     *
     * @param request {@link ChargingSessionRequest} contains stationID to be added
     *                to database.
     * @return {@link ChargingSession} with its attributes.
     * @throws InvalidInputParameterException if stationId is empty or something goes
     *                                   wrong while adding
     */
    ChargingSession saveNewChargingSession(ChargingSessionRequest request) throws InvalidInputParameterException;

    /**
     * Updates {@link ChargingSession} of {@link UUID} with stoppedDateAndTime and
     * Status.
     *
     * @param id {@link UUID}
     * @return {@link ChargingSession} updated charging session
     */
    ChargingSession stopChargingSession(UUID id);

    /**
     * Retrieves all charging sessions.
     *
     * @return {@link ChargingSessionResponse} containing all list of
     * {@link ChargingSession}s
     */
    ChargingSessionResponse retrieveChargingSessions();

    /**
     * This method retrieves summary of submitted charging sessions including:<em>
     * totalCount, startedCount, stoppedCount</em> which represents count of
     * charging session updates <b>for the last minute</b>
     *
     * @return {@link MetricsCount} instance
     */
    MetricsCount retrieveChargingSessionsSummary();

}
