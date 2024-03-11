package com.example.coordinatesconverter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class DMSCoordinates {
    private double latDegrees;
    private double latMinutes;
    private double latSeconds;
    private double lonDegrees;
    private double lonMinutes;
    private double lonSeconds;

    @Override
    public String toString() {
        return String.format("%d° %d' %.5f\", %d° %d' %.5f\"", (int)latDegrees, (int)latMinutes, latSeconds
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
