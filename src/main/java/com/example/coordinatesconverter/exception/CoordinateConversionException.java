package com.example.coordinatesconverter.exception;

public class CoordinateConversionException extends RuntimeException {

    public CoordinateConversionException(String message) {
        super(message);
    }

    public CoordinateConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
