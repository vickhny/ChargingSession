package com.evron.chargingsessionservice.dao;

import com.evron.chargingsessionservice.model.ChargingSession;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <em>In-memory data structure is being used here as repository.</em> DAO layer
 * for containing methods to manipulate {@code ChargingSession} data. To make
 * application <em>Thread-safe</em> , <b>{@link ConcurrentHashMap} </b> is being
 * used.
 */
@Component
public class ChargingSessionDao {

    Map<UUID, ChargingSession> map;

    public ChargingSessionDao() {
        map = new ConcurrentHashMap<>();
    }

    public ChargingSession save(ChargingSession session) {
        return map.put(session.getId(), session);
    }

    public boolean contains(UUID id) {
        return map.containsKey(id);
    }

    public ChargingSession findById(UUID id) {
        return map.get(id);
    }

    public Collection<ChargingSession> findAll() {
        return map.values();
    }

    public int size() {
        return map.size();
    }

}
