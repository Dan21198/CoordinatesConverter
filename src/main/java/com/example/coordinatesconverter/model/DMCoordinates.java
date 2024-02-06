package com.example.coordinatesconverter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DMCoordinates {
    private double latDegrees;
    private double latMinutes;
    private double lonDegrees;
    private double lonMinutes;
}
