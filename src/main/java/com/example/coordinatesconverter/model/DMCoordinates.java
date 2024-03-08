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

    @Override
    public String toString() {
        return String.format("%d° %.5f', %d° %.5f'", (int)latDegrees, latMinutes, (int)lonDegrees, lonMinutes);
    }

    public String toList() {
        return String.format("%d\t%.5f\t%d\t%.5f",
                (int)latDegrees,
                latMinutes,
                (int)lonDegrees,
                lonMinutes
        );
    }
}
