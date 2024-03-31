package com.example.coordinatesconverter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@AllArgsConstructor
public class DMCoordinates {
    private double latDegrees;
    private double latMinutes;
    private double lonDegrees;
    private double lonMinutes;
    public double getLatDegrees() {
        return (int)latDegrees;
    }
    public double getLatMinutes() {
        return new BigDecimal(latMinutes)
                .setScale(7, RoundingMode.HALF_UP).doubleValue();
    }
    public double getLonDegrees() {
        return (int)lonDegrees;
    }

    public double getLonMinutes() {
        return new BigDecimal(lonMinutes)
                .setScale(7, RoundingMode.HALF_UP).doubleValue();
    }
    @Override
    public String toString() {
        return String.format("%d° %.5f'N, %d° %.5f'E", (int)latDegrees, latMinutes, (int)lonDegrees, lonMinutes);
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
