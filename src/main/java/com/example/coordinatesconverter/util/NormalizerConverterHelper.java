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

        return String.format("%d°%d'%.5f\"", (int) longitudeDegrees, (int) longitudeMinutes, longitudeSeconds);
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

        return String.format("%d°%d'%.5f\"", (int) latitudeDegrees, (int) latitudeMinutes, latitudeSeconds);
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

        return String.format("%d°%.5f'", (int) longitudeDegrees, longitudeMinutes);
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

        return String.format("%d°%.5f'", (int) latitudeDegrees, latitudeMinutes);
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

        return String.format("%d°%d'%.5f\"", (int) longitudeDegrees, (int) longitudeMinutes, longitudeSeconds);
    }

    public static String formatLatitudeDMS(DMSCoordinates dmsCoordinates) {
        double latitudeDegrees = dmsCoordinates.getLatDegrees();
        double latitudeMinutes = dmsCoordinates.getLatMinutes();
        double latitudeSeconds = dmsCoordinates.getLatSeconds();

        return String.format("%d°%d'%.5f\"", (int) latitudeDegrees, (int) latitudeMinutes, latitudeSeconds);
    }

    public static String convertDMToDDLon(String matchedLongitude) {
        double longitudeDegrees = extractDegrees(matchedLongitude);
        double longitudeMinutes = extractMinutes(matchedLongitude);
        DMCoordinates dmCoordinates = new DMCoordinates(0, 0, longitudeDegrees, longitudeMinutes);
        DDCoordinates ddCoordinates = coordinateConversionService.convertDMToDD(dmCoordinates);

        return String.valueOf(ddCoordinates.getLongitude());
    }

    public static String convertDMSToDDLon(String matchedLongitude) {
        double longitudeDegrees = extractDegrees(matchedLongitude);
        double longitudeMinutes = extractMinutes(matchedLongitude);
        double longitudeSeconds = extractSeconds(matchedLongitude);
        DMSCoordinates dmsCoordinates = new DMSCoordinates(0, 0,0,
                longitudeDegrees, longitudeMinutes, longitudeSeconds);
        DDCoordinates ddCoordinates = coordinateConversionService.convertDMSToDD(dmsCoordinates);

        return String.valueOf(ddCoordinates.getLongitude());
    }

    private static double extractSeconds(String dmsCoordinate) {
        String[] parts = dmsCoordinate.split("°");
        String secondsPart;
        if (parts[1].contains("\"")) {
            secondsPart = parts[1].substring(parts[1].indexOf("'") + 1, parts[1].indexOf("\""));
        } else {
            secondsPart = parts[1].substring(parts[1].indexOf("'") + 1);
        }
        return Double.parseDouble(secondsPart);
    }

    public static String convertExcelDDToDMS(String cellValue) {
        double ddValue = Double.parseDouble(cellValue);
        DDCoordinates ddCoordinates = new DDCoordinates(ddValue, ddValue);
        DMSCoordinates dmsCoordinates = coordinateConversionService.convertDDToDMS(ddCoordinates);

        return formatLongitudeDMS(dmsCoordinates);
    }

    public static String convertExcelDMToDMS(String cellValue, String nextCellValue) {
        double dmValue = Double.parseDouble(cellValue);
        double nextDmValue = Double.parseDouble(nextCellValue);
        DMCoordinates dmCoordinates = new DMCoordinates(dmValue, nextDmValue, dmValue, nextDmValue);
        DMSCoordinates dmsCoordinates = coordinateConversionService.convertDMToDMS(dmCoordinates);

        return formatLongitudeDMS(dmsCoordinates);
    }

    public static String convertExcelDDoDM(String cellValue) {
        double ddValue = Double.parseDouble(cellValue);
        DDCoordinates ddCoordinates = new DDCoordinates(ddValue, ddValue);
        DMCoordinates dmCoordinates = coordinateConversionService.convertDDToDM(ddCoordinates);

        return formatLongitudeDM(dmCoordinates);
    }

}

