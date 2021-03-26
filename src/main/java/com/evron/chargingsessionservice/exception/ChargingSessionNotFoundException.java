package com.evron.chargingsessionservice.exception;

public class ChargingSessionNotFoundException extends RuntimeException {

    /**
     * @param message exception message
     */
    public ChargingSessionNotFoundException(String message) {
        super(message);
    }

}
