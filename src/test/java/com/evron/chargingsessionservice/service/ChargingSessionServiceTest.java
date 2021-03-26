package com.evron.chargingsessionservice.service;

import com.evron.chargingsessionservice.dao.ChargingSessionDao;
import com.evron.chargingsessionservice.exception.ChargingSessionNotFoundException;
import com.evron.chargingsessionservice.exception.InvalidInputParameterException;
import com.evron.chargingsessionservice.model.ChargingSession;
import com.evron.chargingsessionservice.model.ChargingSessionRequest;
import com.evron.chargingsessionservice.model.ChargingSessionResponse;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.evron.chargingsessionservice.model.ChargingSession.SessionStatus.FINISHED;
import static com.evron.chargingsessionservice.model.ChargingSession.SessionStatus.IN_PROGRESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ChargingSessionServiceTest {

    @InjectMocks
    private ChargingSessionServiceImpl service;

    @Mock
    private ChargingSessionDao dao;

    @Test
    public void testSaveNewChargingSession_Success() {
        String stationId = "ABC-12345";
        ChargingSessionRequest request = new ChargingSessionRequest();
        request.setStationId(stationId);

        when(dao.save(Mockito.any(ChargingSession.class))).thenReturn(mockedSession(stationId));
        ChargingSession response = service.saveNewChargingSession(request);

        verify(dao, times(1)).save(Mockito.any(ChargingSession.class));
        assertNotNull(response);
        assertEquals(stationId, response.getStationId());
        assertEquals(IN_PROGRESS, response.getStatus());
        assertNotNull(response.getStationId());
        assertNull(response.getStoppedAt());

    }

    private ChargingSession mockedSession(String stationId) {
        ChargingSession session = new ChargingSession();
        session.setStationId(stationId);
        session.setId(UUID.randomUUID());
        session.setStatus(IN_PROGRESS);
        session.setStartedAt(LocalDateTime.now());
        return session;
    }

    @Test
    public void testSaveNewChargingSessionThrowsInvalidParameterException() {
        ChargingSessionRequest request = new ChargingSessionRequest();
        request.setStationId("");

        Exception exception = assertThrows(InvalidInputParameterException.class, () -> {
            service.saveNewChargingSession(request);
        });

        verify(dao, times(0)).save(Mockito.any(ChargingSession.class));
        String expectedMessage = "Incorrect/missing stationId";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void testStopChargingSession_Success() {

        String stationId = "ABC-12345";

        ChargingSession mockedSession = mockedSession(stationId);

        when(dao.contains(Mockito.any(UUID.class))).thenReturn(true);
        when(dao.findById(Mockito.any(UUID.class))).thenReturn(mockedSession);

        mockedSession.setStatus(FINISHED);
        mockedSession.setStoppedAt(LocalDateTime.now());
        when(dao.save(Mockito.any(ChargingSession.class))).thenReturn(mockedSession);

        UUID uiD = mockedSession.getId();
        ChargingSession response = service.stopChargingSession(uiD);

        verify(dao, times(1)).save(Mockito.any(ChargingSession.class));
        verify(dao, times(1)).contains(Mockito.any(UUID.class));
        verify(dao, times(1)).findById(Mockito.any(UUID.class));
        assertNotNull(response);
        assertEquals("ABC-12345", response.getStationId());
        assertEquals(FINISHED, response.getStatus());
        assertNotNull(response.getStartedAt());
        assertNotNull(response.getStoppedAt());
    }

    @Test
    public void testStopChargingSessionThrowsChargingSessionNotFoundException() {

        UUID uid = UUID.fromString("f1ac3841-3441-46a7-bb69-017a93a30224");

        Exception exception = assertThrows(ChargingSessionNotFoundException.class, () -> {
            service.stopChargingSession(uid);
        });

        verify(dao, times(0)).save(Mockito.any(ChargingSession.class));
        verify(dao, times(1)).contains(Mockito.any(UUID.class));
        verify(dao, times(0)).findById(Mockito.any(UUID.class));
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(String.format("Charging Session with ID %s does not exist", uid.toString())));
    }

    @Test
    public void testRetrieveChargingSessions() {

        when(dao.size()).thenReturn(2);
        when(dao.findAll()).thenReturn(mockedSessionList());
        ChargingSessionResponse response = service.retrieveChargingSessions();

        verify(dao, times(1)).size();
        verify(dao, times(1)).findAll();
        assertNotNull(response);
        assertEquals(response.getChargingSessions().size(), 2);
    }

    private Collection<ChargingSession> mockedSessionList() {
        ChargingSession session1 = mockedSession("ABC-1234");
        ChargingSession session2 = mockedSession("DEF-1234");
        session2.setStoppedAt(LocalDateTime.now());
        session2.setStatus(FINISHED);
        List<ChargingSession> list = new ArrayList<>();
        list.add(session1);
        list.add(session2);
        return list;
    }

    private Collection<ChargingSession> mockedSessionListForMoreThanAMinute() {
        ChargingSession session1 = mockedSession("LMN-1234");
        session1.setStartedAt(LocalDateTime.MIN);  //session1 is started and not stopped
        ChargingSession session2 = mockedSession("XYZ-1234");
        session2.setStoppedAt(LocalDateTime.now()); //session1 is started and stopped
        session2.setStatus(FINISHED);
        List<ChargingSession> list = new ArrayList<>();
        list.add(session1);
        list.add(session2);
        return list;
    }

}