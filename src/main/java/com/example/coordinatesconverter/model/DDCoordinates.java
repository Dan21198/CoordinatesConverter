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
}
