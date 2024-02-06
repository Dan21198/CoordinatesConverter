package com.example.coordinatesconverter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DMSCoordinates {
    private double latDegrees;
    private double latMinutes;
    private double latSeconds;
    private double lonDegrees;
    private double lonMinutes;
    private double lonSeconds;
}
