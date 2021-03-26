package com.evron.chargingsessionservice.controller;

import com.evron.chargingsessionservice.ChargingsessionserviceApplication;
import com.evron.chargingsessionservice.model.ChargingSession;
import com.evron.chargingsessionservice.model.ChargingSessionRequest;
import com.evron.chargingsessionservice.service.ChargingSessionServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URL;
import java.security.InvalidParameterException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = {ChargingsessionserviceApplication.class})
public class ChargingSessionControllerTest {

    @MockBean
    private ChargingSessionServiceImpl service;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testSaveNewChargingSessionForValidRequest() throws Exception {
        String stationId = "ABC-1234";
        final ChargingSessionRequest request = new ChargingSessionRequest();
        request.setStationId(stationId);
        ChargingSession response = mapper.readValue(new URL("file:src/main/resources/stab/charging-session-response.json"),
                ChargingSession.class);
        Mockito.when(service.saveNewChargingSession(Mockito.any(ChargingSessionRequest.class))).thenReturn(response);
        mockMvc.perform(post("/chargingSessions").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request))).andExpect(status().isOk());

        verify(service, times(1)).saveNewChargingSession(Mockito.any(ChargingSessionRequest.class));

    }

    @Test
    public void testSaveNewChargingSessionThrowsExceptionForInValidRequest() throws Exception {
        String stationId = "";
        final ChargingSessionRequest request = new ChargingSessionRequest();
        request.setStationId(stationId);

        try {
            mockMvc.perform(post("/chargingSessions").contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(request)));
        } catch (Exception e) {
            verify(service, times(0)).saveNewChargingSession(Mockito.any(ChargingSessionRequest.class));
            assertNotNull(e.getCause());
            assertTrue(e.getCause() instanceof InvalidParameterException);
            assertNotNull(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().contains("Incorrect/missing stationId"));
        }
    }

    @Test
    public void testSaveNewChargingSessionReturns400WhenRequestBodyIsNotValidJson() throws Exception {
        mockMvc.perform(post("/chargingSessions").contentType(MediaType.APPLICATION_JSON_VALUE).content(""))
                .andExpect(status().is4xxClientError());
        verify(service, times(0)).saveNewChargingSession(Mockito.any(ChargingSessionRequest.class));
    }

    @Test
    public void testSaveNewChargingSessionReturnsMediaTypeUnsupportedWhenMediaTypeIsNotPassed() throws Exception {
        String stationId = "ABC-1234";
        final ChargingSessionRequest request = new ChargingSessionRequest();
        request.setStationId(stationId);

        mockMvc.perform(post("/chargingSessions").content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnsupportedMediaType());

        verify(service, times(0)).saveNewChargingSession(Mockito.any(ChargingSessionRequest.class));
    }


    @Test
    public void testStopChargingSession() throws Exception {
        ChargingSession response = mapper.readValue(new URL("file:src/main/resources/stab/charging-session-response.json"),
                ChargingSession.class);
        UUID uiD = UUID.fromString("0b6f6f31-13bf-471a-9627-35c62f0ceda9");

        Mockito.when(service.stopChargingSession(Mockito.any(UUID.class))).thenReturn(response);
        mockMvc.perform(put("/chargingSessions/" + uiD))
                .andExpect(status().isOk());

        verify(service, times(1)).stopChargingSession(Mockito.any(UUID.class));
    }
}
