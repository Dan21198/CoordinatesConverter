package com.example.coordinatesconverter.util;

import com.example.coordinatesconverter.model.DDCoordinates;
import com.example.coordinatesconverter.model.DMCoordinates;
import com.example.coordinatesconverter.model.DMSCoordinates;
import com.example.coordinatesconverter.service.CoordinateConversionServiceImpl;

public class NormalizerConverterHelper {
    private static final CoordinateConversionServiceImpl coordinateConversionService
            = new CoordinateConversionServiceImpl();

    public static String convertDDLonToDMSLon(String matchedLongitude) {
        double longitude = Double.parseDouble(matchedLongitude);
        DDCoordinates ddCoordinates = new DDCoordinates(0, longitude);
        DMSCoordinates dmsCoordinates = coordinateConversionService.convertDDToDMS(ddCoordinates);

        return formatLongitude(dmsCoordinates);
    }

    public static String formatLongitude(DMSCoordinates dmsCoordinates) {
        double longitudeDegrees = dmsCoordinates.getLonDegrees();
        double longitudeMinutes = dmsCoordinates.getLonMinutes();
        double longitudeSeconds = dmsCoordinates.getLonSeconds();

        return String.format("%d°%d'%f\"", (int) longitudeDegrees, (int) longitudeMinutes, longitudeSeconds);
    }

    public static String convertDDLatToDMSLat(String matchedLatitude) {
        double latitude = Double.parseDouble(matchedLatitude);
        DDCoordinates ddCoordinates = new DDCoordinates(latitude, 0);
        DMSCoordinates dmsCoordinates = coordinateConversionService.convertDDToDMS(ddCoordinates);

        return formatLatitude(dmsCoordinates);
    }

    public static String formatLatitude(DMSCoordinates dmsCoordinates) {
        double latitudeDegrees = dmsCoordinates.getLatDegrees();
        double latitudeMinutes = dmsCoordinates.getLatMinutes();
        double latitudeSeconds = dmsCoordinates.getLatSeconds();

        return String.format("%d°%d'%f\"", (int) latitudeDegrees, (int) latitudeMinutes, latitudeSeconds);
    }

    public static String convertDDToDMLon(String matchedLongitude) {
        double longitude = Double.parseDouble(matchedLongitude);

        DDCoordinates ddCoordinates = new DDCoordinates(0, longitude);
        DMCoordinates dmCoordinates = coordinateConversionService.convertDDToDM(ddCoordinates);

        return formatLongitudeDM(dmCoordinates);
    }

    public static String formatLongitudeDM(DMCoordinates dmCoordinates) {
        double longitudeDegrees = dmCoordinates.getLonDegrees();
        double longitudeMinutes = dmCoordinates.getLonMinutes();

        return String.format("%d°%f'", (int) longitudeDegrees, longitudeMinutes);
    }

    public static String convertDDToDMLat(String matchedLatitude) {
        double latitude = Double.parseDouble(matchedLatitude);
        DDCoordinates ddCoordinates = new DDCoordinates(latitude, 0);
        DMCoordinates dmCoordinates = coordinateConversionService.convertDDToDM(ddCoordinates);

        return formatLatitudeDM(dmCoordinates);
    }

    public static String formatLatitudeDM(DMCoordinates dmCoordinates) {
        double latitudeDegrees = dmCoordinates.getLatDegrees();
        double latitudeMinutes = dmCoordinates.getLatMinutes();

        return String.format("%d°%f'", (int) latitudeDegrees, latitudeMinutes);
    }

    public static String convertDMToDMSLon(String matchedLongitude) {
        double longitudeDegrees = extractDegrees(matchedLongitude);
        double longitudeMinutes = extractMinutes(matchedLongitude);
        DMSCoordinates dmsCoordinates = coordinateConversionService.convertDMToDMS(new DMCoordinates(0,
                0, longitudeDegrees,longitudeMinutes));

        return formatLongitudeDMS(dmsCoordinates);
    }

    public static String convertDMToDMSLat(String matchedLatitude) {
        double latitudeDegrees = extractDegrees(matchedLatitude);
        double latitudeMinutes = extractMinutes(matchedLatitude);
        DMSCoordinates dmsCoordinates = coordinateConversionService.convertDMToDMS(new DMCoordinates(latitudeDegrees,
                latitudeMinutes, 0,0));

        return formatLatitudeDMS(dmsCoordinates);
    }

    private static double extractDegrees(String dmCoordinate)
    {
        return Double.parseDouble(dmCoordinate.split("°")[0]);
    }

    private static double extractMinutes(String dmCoordinate) {
        String[] parts = dmCoordinate.split("°");
        String minutesPart = parts[1].substring(0, parts[1].indexOf("'"));
        return Double.parseDouble(minutesPart);
    }

    public static String formatLongitudeDMS(DMSCoordinates dmsCoordinates) {
        double longitudeDegrees = dmsCoordinates.getLonDegrees();
        double longitudeMinutes = dmsCoordinates.getLonMinutes();
        double longitudeSeconds = dmsCoordinates.getLonSeconds();

        return String.format("%d°%d'%f\"", (int) longitudeDegrees, (int) longitudeMinutes, longitudeSeconds);
    }

    public static String formatLatitudeDMS(DMSCoordinates dmsCoordinates) {
        double latitudeDegrees = dmsCoordinates.getLatDegrees();
        double latitudeMinutes = dmsCoordinates.getLatMinutes();
        double latitudeSeconds = dmsCoordinates.getLatSeconds();

        return String.format("%d°%d'%f\"", (int) latitudeDegrees, (int) latitudeMinutes, latitudeSeconds);
    }


}

