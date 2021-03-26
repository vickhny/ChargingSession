package com.evron.chargingsessionservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model class for ChargingSession. JSON representation of this class would look
 * like:
 *
 * <pre>
 * {
 * "id": "0b6f6f31-13bf-471a-9627-35c62f0ceda9",
 * "stationId": "ABC-12345",
 * "startedAt": "2021-03-15T14:44:30.438609",
 * "stoppedAt": "2021-03-15T14:44:43.1721144",
 * "status": "FINISHED"
 * }
 * </pre>
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class ChargingSession {

    UUID id;
    String stationId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime startedAt;

    @JsonInclude(value = Include.NON_NULL)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime stoppedAt;
    SessionStatus status;

    public enum SessionStatus {

        IN_PROGRESS, FINISHED;
    }

}
