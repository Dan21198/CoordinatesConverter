package com.example.coordinatesconverter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Data
@AllArgsConstructor
public class DMSCoordinates {
    private double latDegrees;
    private double latMinutes;
    private double latSeconds;
    private double lonDegrees;
    private double lonMinutes;
    private double lonSeconds;

    public double getLatDegrees() {
        return (int)latDegrees;
    }

    public double getLatMinutes() {
        return (int)latMinutes;
    }

    public double getLatSeconds() {
        return new BigDecimal(latSeconds).setScale(7, RoundingMode.HALF_UP).doubleValue();
    }

    public double getLonDegrees() {
        return (int)lonDegrees;
    }

    public double getLonMinutes() {
        return (int)lonMinutes;
    }

    public double getLonSeconds() {
        return new BigDecimal(lonSeconds).setScale(7, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public String toString() {
        return String.format("%d° %d' %.5f\"N, %d° %d' %.5f\"E", (int)latDegrees, (int)latMinutes, latSeconds
                , (int)lonDegrees, (int)lonMinutes, lonSeconds);
    }

    public String toList() {
        return String.format("%d\t%d\t%.5f\t%d\t%d\t%.5f",
                (int)latDegrees,
                (int)latMinutes,
                latSeconds,
                (int)lonDegrees,
                (int)lonMinutes,
                lonSeconds
        );
    }
}
