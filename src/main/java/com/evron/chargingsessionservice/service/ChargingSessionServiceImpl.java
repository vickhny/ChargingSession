package com.evron.chargingsessionservice.service;

import com.evron.chargingsessionservice.dao.ChargingSessionDao;
import com.evron.chargingsessionservice.exception.ChargingSessionNotFoundException;
import com.evron.chargingsessionservice.exception.InvalidInputParameterException;
import com.evron.chargingsessionservice.model.ChargingSession;
import com.evron.chargingsessionservice.model.ChargingSessionRequest;
import com.evron.chargingsessionservice.model.ChargingSessionResponse;
import com.evron.chargingsessionservice.model.MetricsCount;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import static com.evron.chargingsessionservice.model.ChargingSession.SessionStatus.FINISHED;
import static com.evron.chargingsessionservice.model.ChargingSession.SessionStatus.IN_PROGRESS;

/**
 * Implementation of {@link ChargingSessionService}
 */
@Service
@Slf4j
public class ChargingSessionServiceImpl implements ChargingSessionService {

    /**
     * {@code ChargingSessionDao} instance
     */
    private ChargingSessionDao chargingSessionDao;

    public ChargingSessionServiceImpl(ChargingSessionDao chargingSessionDao) {
        this.chargingSessionDao = chargingSessionDao;
    }

    /**
     * {@inheritDoc}
     *
     * @see com.evron.chargingsessionservice.service.ChargingSessionService#saveNewChargingSession(ChargingSessionRequest)
     */
    @Override
    public ChargingSession saveNewChargingSession(ChargingSessionRequest request) throws InvalidInputParameterException {
        ChargingSession session = null;
        if (request != null) {
            String stationId = request.getStationId();
            if (stationId.isEmpty()) {
                throw new InvalidInputParameterException("Incorrect/missing stationId");
            }
            session = constructChargingSessionEntity(request);
            log.debug("Adding/Saving new charging station");
            chargingSessionDao.save(session);
        }

        return session;
    }


    /**
     * {@inheritDoc}
     *
     * @see com.evron.chargingsessionservice.service.ChargingSessionService#stopChargingSession(UUID))
     */
    @Override
    public ChargingSession stopChargingSession(UUID id) {

        ChargingSession stopSession = null;
        if (id != null) {
            if (chargingSessionDao.contains(id)) {
                stopSession = chargingSessionDao.findById(id);
                updateChargingSessionEntity(stopSession);
                chargingSessionDao.save(stopSession);
            } else {
                throw new ChargingSessionNotFoundException(
                        String.format("Charging Session with ID %s does not exist", id.toString()));
            }
        }
        return stopSession;
    }


    /**
     * {@inheritDoc}
     *
     * {@see io.everon.assignment.service.ChargingSessionService#retrieveChargingSessions()
     */
    @Override
    public ChargingSessionResponse retrieveChargingSessions() {
        log.debug("Retrieving all charging sessions");
        ChargingSessionResponse response = constructChargingSessionResponse();
        return response;
    }

    /**
     * {@inheritDoc}
     *
     * {@see io.everon.assignment.service.ChargingSessionService#retrieveChargingSessionsSummary()
     */
    @Override
    public MetricsCount retrieveChargingSessionsSummary() {
        AtomicInteger startedCount = new AtomicInteger();
        AtomicInteger stoppedCount = new AtomicInteger();
        MetricsCount count = new MetricsCount();

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime previousTime = currentTime.minusMinutes(1);

        //Time Complexity - O(n)
        chargingSessionDao.findAll().stream().forEach(session ->{
            if ((session.getStartedAt().isBefore(currentTime) && session.getStartedAt().isAfter(previousTime))
                    || (session.getStoppedAt() != null && session.getStoppedAt().isBefore(currentTime)
                    && session.getStoppedAt().isAfter(previousTime))){
                if (session.getStartedAt() != null)
                    startedCount.getAndIncrement();
                if (session.getStoppedAt() != null)
                    stoppedCount.getAndIncrement();
            }
        });

        count.setStartedCount(startedCount.get());
        count.setStoppedCount(stoppedCount.get());
        count.setTotalCount(startedCount.get() + stoppedCount.get());
        return count;
    }

    /**
     * Constructs {@link ChargingSession} from {@link ChargingSessionRequest}
     *
     * @param request {@code ChargingSessionRequest} containing stationID
     * @return {@code ChargingSession} to be stored in database.
     */
    private ChargingSession constructChargingSessionEntity(ChargingSessionRequest request) {
        ChargingSession session = new ChargingSession();
        session.setId(UUID.randomUUID());
        session.setStationId(request.getStationId());
        session.setStartedAt(LocalDateTime.now());
        session.setStatus(IN_PROGRESS);
        return session;
    }


    /**
     * Prepares {@link ChargingSessionResponse} from List of ChargingSession
     *
     * @return {@code ChargingSessionResponse}
     */
    private ChargingSessionResponse constructChargingSessionResponse() {
        ChargingSessionResponse response = new ChargingSessionResponse();
        if (chargingSessionDao.size() != 0) {
            List<ChargingSession> list = chargingSessionDao.findAll().parallelStream().collect(Collectors.toList());
            response.setChargingSessions(list);
        }
        return response;
    }

    /**
     * Updating {@link ChargingSession} StoppedAT and Status Attribute
     *
     * @param oldSession {@code ChargingSession} instance
     */
    private void updateChargingSessionEntity(ChargingSession oldSession) {
        oldSession.setStoppedAt(LocalDateTime.now());
        oldSession.setStatus(FINISHED);
    }


}
