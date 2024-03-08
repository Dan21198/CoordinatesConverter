package com.example.coordinatesconverter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DDCoordinates {
    private double latitude;
    private double longitude;
    @Override
    public String toString() {
        return String.format("%.5f, %.5f", latitude, longitude);
    }

    public String toList() {
        return String.format("%.5f\t%.5f",
                latitude,
                longitude
        );
    }
}
