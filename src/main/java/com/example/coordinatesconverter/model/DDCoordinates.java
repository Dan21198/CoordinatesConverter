package com.example.coordinatesconverter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Builder
@AllArgsConstructor
public class DDCoordinates {
    private double latitude;
    private double longitude;
    public double getLatitude() {
        return new BigDecimal(latitude)
                .setScale(7, RoundingMode.HALF_UP).doubleValue();
    }

    public double getLongitude() {
        return new BigDecimal(longitude)
                .setScale(7, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public String toString() {
        return String.format("%.5f°N, %.5f°E", latitude, longitude);
    }

    public String toList() {
        return String.format("%.5f\t%.5f",
                latitude,
                longitude
        );
    }
}
