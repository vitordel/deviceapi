package com.vitordel.deviceapi.exception;

import java.util.UUID;

public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(String message) {
        super(message);
    }
}
