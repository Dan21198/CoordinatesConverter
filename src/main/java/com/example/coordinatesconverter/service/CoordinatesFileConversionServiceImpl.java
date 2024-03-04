package com.example.coordinatesconverter.service;

import com.example.coordinatesconverter.model.DDCoordinates;
import com.example.coordinatesconverter.model.DMCoordinates;
import com.example.coordinatesconverter.model.DMSCoordinates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class CoordinatesFileConversionServiceImpl {

    private final CoordinateConversionServiceImpl coordinateConversionService;

    public String convertCoordinates(String conversionType, String coordinates) {
        if (coordinates.isEmpty()) {
            throw new IllegalArgumentException("Coordinates cannot be empty");
        }

        String[] splitCoordinates = coordinates.replaceAll("[NSWE]", "").split(",");
        String lat = splitCoordinates[0].trim();
        String lon = splitCoordinates[1].trim();

        boolean isDMS = lat.contains("°") && lat.contains("'") && lat.contains("\"");
        boolean isDM = lat.contains("°") && lat.contains("'") && !lat.contains("\"");
        boolean isDD = lat.contains("°") && !lat.contains("'") && !lat.contains("\"");

        if (!isDMS && !isDM && !isDD) {
            throw new IllegalArgumentException("Invalid coordinate format" + coordinates);
        }

        switch (conversionType) {
            case "DD":
                if (isDMS) {
                    DMSCoordinates dmsCoordinates = new DMSCoordinates(extractDegrees(lat), extractMinutes(lat),
                            extractSeconds(lat), extractDegrees(lon), extractMinutes(lon), extractSeconds(lon));
                    DDCoordinates ddCoordinates = coordinateConversionService.convertDMSToDD(dmsCoordinates);
                    return formatDDCoordinates(ddCoordinates);
                } else if (lat.contains("°") && lat.contains("'")) {
                    DMCoordinates dmCoordinates = new DMCoordinates(extractDegrees(lat), extractMinutes(lat),
                            extractDegrees(lon), extractMinutes(lon));
                    DDCoordinates ddCoordinates = coordinateConversionService.convertDMToDD(dmCoordinates);
                    return formatDDCoordinates(ddCoordinates);
                }
                break;
            case "DM":
                if (isDMS) {
                    DMSCoordinates dmsCoordinates = new DMSCoordinates(extractDegrees(lat), extractMinutes(lat),
                            extractSeconds(lat), extractDegrees(lon), extractMinutes(lon), extractSeconds(lon));
                    DMCoordinates dmCoordinates = coordinateConversionService.convertDMSToDM(dmsCoordinates);
                    return formatDMCoordinates(dmCoordinates);
                } else if (lat.contains("°") && !lat.contains("'")) {
                    DDCoordinates ddCoordinates = new DDCoordinates(Double.parseDouble(lat.replace("°",
                            "")),
                            Double.parseDouble(lon.replace("°", "")));
                    DMCoordinates dmCoordinates = coordinateConversionService.convertDDToDM(ddCoordinates);
                    return formatDMCoordinates(dmCoordinates);
                }
                break;
            case "DMS":
                if (isDM) {
                    DMCoordinates dmCoordinates = new DMCoordinates(extractDegrees(lat), extractMinutes(lat),
                            extractDegrees(lon), extractMinutes(lon));
                    DMSCoordinates dmsCoordinates = coordinateConversionService.convertDMToDMS(dmCoordinates);
                    return formatDMSCoordinates(dmsCoordinates);
                } else if (lat.contains("°") && !lat.contains("'")) {
                    DDCoordinates ddCoordinates = new DDCoordinates(Double.parseDouble(lat.replace("°",
                            ""))
                            , Double.parseDouble(lon.replace("°", "")));
                    DMSCoordinates dmsCoordinates = coordinateConversionService.convertDDToDMS(ddCoordinates);
                    return formatDMSCoordinates(dmsCoordinates);
                }
                break;
        }
        return coordinates;
    }

    private double extractDegrees(String coordinate) {
        int degreeIndex = coordinate.indexOf("°");
        if (degreeIndex != -1) {
            return Double.parseDouble(coordinate.substring(0, degreeIndex));
        }
        throw new IllegalArgumentException("Invalid coordinate format");
    }

    private double extractMinutes(String coordinate) {
        return Double.parseDouble(coordinate.substring(coordinate.indexOf("°") + 1, coordinate.indexOf("'")));
    }

    private double extractSeconds(String coordinate) {
        return Double.parseDouble(coordinate.substring(coordinate.indexOf("'") + 1, coordinate.indexOf("\"")));
    }

    private String formatDDCoordinates(DDCoordinates ddCoordinates) {
        return String.format("%.5f°N,", ddCoordinates.getLatitude())
                + String.format("%.5f°E", ddCoordinates.getLongitude());
    }

    private String formatDMCoordinates(DMCoordinates dmCoordinates) {
        return String.format("%d°", (int) dmCoordinates.getLatDegrees())
                + String.format("%.5f'N,", dmCoordinates.getLatMinutes())
                + String.format("%d°", (int) dmCoordinates.getLonDegrees())
                + String.format("%.5f'E", dmCoordinates.getLonMinutes());
    }

    private String formatDMSCoordinates(DMSCoordinates dmsCoordinates) {
        return String.format("%d°", (int) dmsCoordinates.getLatDegrees())
                + String.format("%d'%.5f\"N,", (int) dmsCoordinates.getLatMinutes(), dmsCoordinates.getLatSeconds())
                + String.format("%d°", (int) dmsCoordinates.getLonDegrees())
                + String.format("%d'%.5f\"E", (int) dmsCoordinates.getLonMinutes(), dmsCoordinates.getLonSeconds());
    }
}