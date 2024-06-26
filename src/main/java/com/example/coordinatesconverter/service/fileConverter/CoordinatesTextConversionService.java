package com.example.coordinatesconverter.service.fileConverter;

import com.example.coordinatesconverter.model.DDCoordinates;
import com.example.coordinatesconverter.model.DMCoordinates;
import com.example.coordinatesconverter.model.DMSCoordinates;
import com.example.coordinatesconverter.service.converter.CoordinateConversionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CoordinatesTextConversionService {

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
                    return ddCoordinates.toString();
                } else if (isDM) {
                    DMCoordinates dmCoordinates = new DMCoordinates(extractDegrees(lat), extractMinutes(lat),
                            extractDegrees(lon), extractMinutes(lon));
                    DDCoordinates ddCoordinates = coordinateConversionService.convertDMToDD(dmCoordinates);
                    return ddCoordinates.toString();
                }
                break;
            case "DM":
                if (isDMS) {
                    DMSCoordinates dmsCoordinates = new DMSCoordinates(extractDegrees(lat), extractMinutes(lat),
                            extractSeconds(lat), extractDegrees(lon), extractMinutes(lon), extractSeconds(lon));
                    DMCoordinates dmCoordinates = coordinateConversionService.convertDMSToDM(dmsCoordinates);
                    return dmCoordinates.toString();
                } else if (isDD) {
                    DDCoordinates ddCoordinates = new DDCoordinates(Double.parseDouble(lat.replace("°",
                            "")),
                            Double.parseDouble(lon.replace("°", "")));
                    DMCoordinates dmCoordinates = coordinateConversionService.convertDDToDM(ddCoordinates);
                    return dmCoordinates.toString();
                }
                break;
            case "DMS":
                if (isDM) {
                    DMCoordinates dmCoordinates = new DMCoordinates(extractDegrees(lat), extractMinutes(lat),
                            extractDegrees(lon), extractMinutes(lon));
                    DMSCoordinates dmsCoordinates = coordinateConversionService.convertDMToDMS(dmCoordinates);
                    return dmsCoordinates.toString();
                } else if (isDD) {
                    DDCoordinates ddCoordinates = new DDCoordinates(Double.parseDouble(lat.replace("°",
                            ""))
                            , Double.parseDouble(lon.replace("°", "")));
                    DMSCoordinates dmsCoordinates = coordinateConversionService.convertDDToDMS(ddCoordinates);
                    return dmsCoordinates.toString();
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

}