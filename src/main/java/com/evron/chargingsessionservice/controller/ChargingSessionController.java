package com.evron.chargingsessionservice.controller;

import com.evron.chargingsessionservice.model.ChargingSession;
import com.evron.chargingsessionservice.model.ChargingSessionRequest;
import com.evron.chargingsessionservice.model.ChargingSessionResponse;
import com.evron.chargingsessionservice.model.MetricsCount;
import com.evron.chargingsessionservice.service.ChargingSessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * REST controller for ChargingSessions
 */
@RestController
@Api(value = "Charging Session Service")
@RequestMapping(value = "chargingSessions")
public class ChargingSessionController {

    /**
     * {@code ChargingSessionService} instance
     */
    private ChargingSessionService service;

    @Autowired
    public ChargingSessionController(ChargingSessionService service) {
        this.service = service;
    }

    /**
     * This method retrieves all charging sessions.
     *
     * @return {@link ChargingSessionResponse} wrapped in {@link ResponseEntity}
     */
    @ApiOperation(value = "Retrieve all charging sessions")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> retriveChargingSessions() {
        ChargingSessionResponse response = service.retrieveChargingSessions();
        if (ObjectUtils.isEmpty(response.getChargingSessions())) {
            return ResponseEntity.ok("No sessions found!!");
        }
        return ResponseEntity.ok(service.retrieveChargingSessions());
    }

    /**
     * This method adds new charging session for the station. Request body will be
     * validated and deserialized to {@link ChargingSessionRequest}
     *
     * @param request {@link ChargingSessionRequest}
     * @return {@link ChargingSession} wrapped in {@link ResponseEntity}
     */
    @ApiOperation(value = "Submit a new charging session for the station")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChargingSession> createChargingSession(@RequestBody ChargingSessionRequest request) {
        return ResponseEntity.ok(service.saveNewChargingSession(request));
    }

    /**
     * This method stops the charging session.
     *
     * @param id {@link UUID} for which Charging Session will be stopped.
     * @return {@link ChargingSession} wrapped in {@link ResponseEntity}
     */
    @ApiOperation(value = "Stop charging session")
    @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChargingSession> stopChargingSession(@PathVariable UUID id) {
        return ResponseEntity.ok(service.stopChargingSession(id));
    }

    /**
     * This method retrieves summary of submitted charging sessions including: <br>
     * <b>totalCount</b>- <em>total number of charging session updates <b>for the
     * last minute</b></br>
     * <br>
     * <b>startedCount</b>- <em>total number of started charging session updates
     * <b>for the last minute</b></br>
     * <br>
     * <b>stoppedCount</b>- <em>total number of stopped charging session updates
     * <b>for the last minute</b></br>
     *
     * @return {@link MetricsCount} wrapped in {@link ResponseEntity}
     */
    @ApiOperation(value = "Retrieve a summary of submitted charging sessions")
    @GetMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MetricsCount> retriveSessionsSummary() {
        return ResponseEntity.ok(service.retrieveChargingSessionsSummary());

    }

}
