package com.example.coordinatesconverter.service;

import com.example.coordinatesconverter.model.DDCoordinates;
import com.example.coordinatesconverter.model.DMCoordinates;
import com.example.coordinatesconverter.model.DMSCoordinates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoordinatesExcelConversionService {

    private final CoordinateConversionServiceImpl coordinateConversionService;

    public List<String> convertCoordinates(String conversionType, List<String> coordinates) {
        if (coordinates.isEmpty()) {
            throw new IllegalArgumentException("Coordinates cannot be empty");
        }

        List<String> convertedCoordinates = new ArrayList<>();

        for (String coordinate : coordinates) {
            if (coordinate.matches(".*[a-zA-Z]+.*")) {
                convertedCoordinates.add(coordinate);
                continue;
            }
            coordinate = coordinate.replace(",", ".");
            String[] parts = coordinate.split("\t");
            int textColumnCount = parts.length;

            switch (conversionType) {
                case "DD":
                    if (textColumnCount == 2){
                        DDCoordinates ddCoordinates = new DDCoordinates(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
                        convertedCoordinates.add(ddCoordinates.toList());
                    }
                    if (textColumnCount == 4) {
                        DMCoordinates dmCoordinates = new DMCoordinates(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]),
                                Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
                        DDCoordinates ddCoordinates = coordinateConversionService.convertDMToDD(dmCoordinates);
                        convertedCoordinates.add(ddCoordinates.toList());
                    }
                    if (textColumnCount == 6) {
                        DMSCoordinates dmsCoordinates = new DMSCoordinates(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]),
                                Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Double.parseDouble(parts[4]), Double.parseDouble(parts[5]));
                        DDCoordinates ddCoordinates = coordinateConversionService.convertDMSToDD(dmsCoordinates);
                        convertedCoordinates.add(ddCoordinates.toList());
                    }
                    break;
                case "DM":
                    if (textColumnCount == 2) {
                        DDCoordinates ddCoordinates = new DDCoordinates(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
                        DMCoordinates dmCoordinates = coordinateConversionService.convertDDToDM(ddCoordinates);
                        convertedCoordinates.add(dmCoordinates.toList());
                    }
                    if (textColumnCount == 4) {
                        DMCoordinates dmCoordinates = new DMCoordinates(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]),
                                Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
                        convertedCoordinates.add(dmCoordinates.toList());
                    }
                    if (textColumnCount == 6) {
                        DMSCoordinates dmsCoordinates = new DMSCoordinates(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]),
                                Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Double.parseDouble(parts[4]), Double.parseDouble(parts[5]));
                        DMCoordinates dmCoordinates = coordinateConversionService.convertDMSToDM(dmsCoordinates);
                        convertedCoordinates.add(dmCoordinates.toList());
                    }
                    break;
                case "DMS":
                    if (textColumnCount == 2) {
                        DDCoordinates ddCoordinates = new DDCoordinates(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
                        DMSCoordinates dmsCoordinates = coordinateConversionService.convertDDToDMS(ddCoordinates);
                        convertedCoordinates.add(dmsCoordinates.toList());
                    }
                    if (textColumnCount == 4) {
                        DMCoordinates dmCoordinates = new DMCoordinates(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]),
                                Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
                        DMSCoordinates dmsCoordinates = coordinateConversionService.convertDMToDMS(dmCoordinates);
                        convertedCoordinates.add(dmsCoordinates.toList());
                    }
                    if (textColumnCount == 6) {
                        DMSCoordinates dmsCoordinates = new DMSCoordinates(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]),
                                Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Double.parseDouble(parts[4]), Double.parseDouble(parts[5]));
                        convertedCoordinates.add(dmsCoordinates.toList());
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid conversion type");
            }
        }


        return convertedCoordinates;
    }


}