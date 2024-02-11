package com.example.coordinatesconverter.util;

import com.example.coordinatesconverter.model.DDCoordinates;
import com.example.coordinatesconverter.model.DMSCoordinates;
import com.example.coordinatesconverter.service.CoordinateConversionService;
import com.example.coordinatesconverter.service.CoordinateConversionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class CoordinateNormalizer {

    public String normalizeCoordinatesText(String text) {
        text = normalizeDD(text);
        text = normalizeDM(text);
        text = normalizeDMS(text);
        text = normalizeMixedCoordinates(text);
        //convertDDLonToDMSLon("49°50'26.45304\"N,18.2883317°E");
        return text;
    }

    private String normalizeDD(String text) {
        // Replace whitespace before North (N) or South (S) with degree symbol (°) and the direction
        text = text.replaceAll("(\\b[NS])\\s+", "°$1 ");

        // Add degree symbol (°) between the numeric value and the direction (N/S/W/E)
        text = text.replaceAll("(\\d+(\\.\\d+)?)\\s*°?\\s*([NSWE])", "$1°$3");

        // Remove all whitespace
        text = text.replaceAll("\\s+", "");

        // Replace commas, semicolons, or multiple spaces with a single comma
        text = text.replaceAll("[,;\\s]+", ",");

        // Replace specific quotation marks with standard double quotes
        text = text.replaceAll("“", "\"");
        text = text.replaceAll("´", "'");
        text = text.replaceAll("´´", "\"");
        text = text.replaceAll("''", "\"");

        // Trim leading and trailing commas, semicolons, or spaces
        text = text.replaceAll("^[,;\\s]+|[;\\s]+$", "");

        // Ensure negative sign is correctly placed before longitude value
        text = text.replaceAll("(?<![\\d-])(-)(-?\\d+\\.\\d+°[NS]),", "$1$2,");

        // Replace multiple consecutive dashes with a single comma
        text = text.replaceAll("(?<!-)-(?!-)|--(?!-)", ",-");

        // Remove trailing comma after dash
        text = text.replaceAll("-,", "-");

        // Remove consecutive commas
        text = text.replaceAll(",,+", ",");

        // Remove trailing comma
        text = text.replaceAll(",$", "");

        // Ensure negative sign is correctly placed before longitude value
        text = text.replaceAll(",-(\\d)", ",-$1");

        // Remove consecutive commas
        text = text.replaceAll(",,+", ",");

        // Handle decimal degrees format with direction before longitude
        text = text.replaceAll("([NSWE])\\.,(\\d)", "$1,$2");

        // Handle decimal degrees format with direction before longitude
        text = text.replaceAll("([NSWE])\\.(\\d)", "$1,$2");

        // Handle missing °E by adding °N and °E
        text = text.replaceAll("(\\d+\\.\\d+),(\\d+\\.\\d+)°E", "$1°N,$2°E");

        // Handle swapped °E and °N
        text = text.replaceAll("(\\d+\\.\\d+)°E,(\\d+\\.\\d+)°N", "$2°N,$1°E");

        // Handle missing °E by adding °E
        text = text.replaceAll("(\\d+\\.\\d+°[NSWE]?),([\\d.]++)(?!°E|°W)", "$1,$2°E");

        // Handle missing °N by adding °E
        text = text.replaceAll("(\\d+\\.\\d+°[NSWE]?),(E|W)?(\\d+\\.\\d+)", "$1,$3°E");

        // Handle missing °N and °E by adding both
        text = text.replaceAll("(\\d+\\.\\d+),(\\d+\\.\\d+)", "$1°N,$2°E");

        // Handle format with direction before longitude
        text = text.replaceAll("([NS])(\\d+\\.\\d+),([EW])(\\d+\\.\\d+)", "$2°$1,$4°$3");

        // Handle format with missing direction indicators
        text = text.replaceAll("(N)?(\\d+\\.\\d+)°(?:N|S)?, (\\d+\\.\\d+)°(?:E|W)?\n", "$2°$1, $4°$5");

        // Handle specific format with N and E before the numbers
        text = text.replaceAll("N,(\\d+\\.\\d+),E,(\\d+\\.\\d+)", "$1°N,$2°E");

        // Replace commas between decimal numbers with dots
        text = text.replaceAll("(\\d+),(\\d+)(°[NS]?),(\\d+),(\\d+)(°[EW]?)", "$1.$2$3,$4.$5$6");

        // Replace commas between decimal numbers with dots
        text = text.replaceAll("(\\d+)(°\\d+'\\d+),(\\d+)([NS]?),(\\d+)(°\\d+'\\d+),(\\d+)([EW]?)", "$1$2.$3$4,$5$6.$7$8");

        // Handle missing commas after N and S in decimal degrees format
        text = text.replaceAll("((\\d+).(\\d+)(°[NS]?))((\\d+).(\\d+)(°[EW]?))", "$1,$5");

        // Handle missing °N by adding °N
        text = text.replaceAll("(\\d+\\.\\d+°(?!N)),((\\d+\\.\\d+°(?=E))E)", "$1N,$2");

        // Handle missing ° before direction
        text = text.replaceAll("°([NSWE])(\\d+\\.\\d+)", "$2°$1,");

        // Remove direction indicators
        text = text.replaceAll("(?<![°'\"])[NSWE]", "");

        // Remove comma before text
        text = text.replaceAll("(?<![^,]),", "");

        // Remove leading degree symbol
        text = text.replaceAll("^°", "");

        // Remove degree symbol after double quote
        text = text.replaceAll("\"°", "\"");

        // Remove trailing characters that are not 'E' or 'W'
        text = text.replaceAll("[^EW]$", "");

        // Handle duplicate direction indicators
        text = text.replaceAll("(°[NSWE])(°[NSWE])", "$2");

        return text;
    }

    private String normalizeDM(String text) {
        // Remove any extra commas that might result from the replacements
        text = text.replaceAll(",,+", ",");

        // Handle the case where °E is missing
        text = text.replaceAll("(\\d+\\.\\d+),(\\d+\\.\\d+)°E", "$1°N,$2°E");

        return text;
    }

    private String normalizeDMS(String text) {
        // Add " before N, S, E, W in DMS
        text = text.replaceAll("((\\d+°)(\\d+'\\d+(\\.\\d+)?\")[NSWE])", "$1,");

        // Remove commas, semicolons, or spaces at the beginning or end of the text
        text = text.replaceAll("^[,;\\s]+|[.,;'\"\\s]+$", "");

        // Replace multiple spaces, commas, or semicolons with a single comma
        text = text.replaceAll("[,;\\s]+", ",");

        // Replace '-,' with a single '-'
        text = text.replaceAll("-,", "-");

        // Remove any extra commas that might result from the replacements
        text = text.replaceAll(",,+", ",");

        // Remove the extra comma if it appears at the end of the text
        text = text.replaceAll(",$", "");

        // Correct the negative sign before the longitude value
        text = text.replaceAll(",-(\\d)", ",-$1");

        // Remove any extra commas that might result from the corrections
        text = text.replaceAll(",,+", ",");

        // Handle the case where °N is missing
        text = text.replaceAll("(\\d+\\.\\d+°[NSWE]?),([\\d.]++)(?!°E|°W)", "$1,$2°E");

        text = text.replaceAll("(\\d+\\.\\d+°),(\\d+\\.\\d+°E)", "$1N,$2");

        // Handle the case where °N is missing
        text = text.replaceAll("(\\d+\\.\\d+°[NSWE]?),(E|W)?(\\d+\\.\\d+)", "$1,$3°E");

        // Handle the case where both latitude and longitude are missing directional indicators
        text = text.replaceAll("(\\d+\\.\\d+),(\\d+\\.\\d+)", "$1°N,$2°E");

        // Handle the case where both latitude and longitude are missing directional indicators
        text = text.replaceAll("(N)?(\\d+\\.\\d+)°(?:N|S)?, (\\d+\\.\\d+)°(?:E|W)?\n", "$2°$1, $4°$5");

        // Replace , symbols with . in decimal number in DMS
        text = text.replaceAll("(\\d+)(°\\d+'\\d+),(\\d+)([NS]?),(\\d+)(°\\d+'\\d+),(\\d+)([EW]?)",
                "$1$2.$3$4,$5$6.$7$8");

        // Add missing , after N and S in DMS
        text = text.replaceAll("(\\d+°\\d+'\\d+\\.\\d+\")(N|S)(\\d+°\\d+'\\d+\\.\\d+\")(E|W)"
                , "$1$2,$3$4");

        // Handle case  x°x'x.x°N,x°E°x'x.x°E -> x°x'x.x°N,x°x'x.x°E
        text = text.replaceAll("(\\d+°\\d+'\\d+\\.\\d+°)([NS]),(\\d+)(°[EW])(°\\d+'\\d+\\.\\d+°[EW])"
                , "$1$2,$3$5");

        // Handle case x°x'x.x°N,x°x'x.x°E -> x°x'x.x"N,x°x'x.x"E
        text = text.replaceAll("(\\d+°\\d+'\\d+\\.\\d+)°([NS]),(\\d+°\\d+'\\d+\\.\\d+)°([EW])"
                , "$1\"$2,$3\"$4");

        // Fix doubled °NSWE
        text = text.replaceAll("(°[NSWE])(°[NSWE])", "$2");

        // Fix ,- to - at beginning of line
        text = text.replaceAll("(?m)^(,-)", "-");

        return text;
    }

    private String normalizeMixedCoordinates(String text) {
        String regex = "\\b(\\d+°\\d+'\\d+\\.\\d+\"[NS],(\\d+\\.\\d+))°([EW])\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        StringBuilder result = new StringBuilder();
        int lastIndex = 0;
        while (matcher.find()) {
            String matchedLongitude = matcher.group(2);
            String convertedLongitude = convertDDLonToDMSLon(matchedLongitude);
            System.out.println(convertedLongitude);
            result.append(text, lastIndex, matcher.start(2));
            result.append(convertedLongitude);
            result.append(matcher.group(3)); // Append direction (E/W)
            lastIndex = matcher.end(3); // Update lastIndex to the end of direction
        }
        result.append(text, lastIndex, text.length());

        return result.toString();
    }




    private static String convertDDLonToDMSLon(String matchedLongitude) {
        CoordinateConversionServiceImpl coordinateConversionService = new CoordinateConversionServiceImpl();
        double longitude = Double.parseDouble(matchedLongitude);
        DDCoordinates ddCoordinates = new DDCoordinates(0, longitude);
        DMSCoordinates dmsCoordinates = coordinateConversionService.convertDDToDMS(ddCoordinates);

        String formattedLongitude = formatLongitude(dmsCoordinates);
        return formattedLongitude;
    }

    private static String formatLongitude(DMSCoordinates dmsCoordinates) {
        double longitudeDegrees = dmsCoordinates.getLonDegrees();
        double longitudeMinutes = dmsCoordinates.getLonMinutes();
        double longitudeSeconds = dmsCoordinates.getLonSeconds();

        return String.format("%d°%d'%f\"", (int) longitudeDegrees, (int) longitudeMinutes, longitudeSeconds);
    }

    private static String convertDDLatToDMSLat(String text) {
        CoordinateConversionServiceImpl coordinateConversionService = new CoordinateConversionServiceImpl();

        String regex = "\\b(\\d+\\.\\d+)°[NS],\\d+°\\d+'\\d+\\.\\d+\"[EW]\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            String latitudeString = matcher.group(1);
            double latitude = Double.parseDouble(latitudeString);
            DDCoordinates ddCoordinates = new DDCoordinates(latitude, 0);
            DMSCoordinates dmsCoordinates = coordinateConversionService.convertDDToDMS(ddCoordinates);

            String formattedLatitude = formatLatitude(dmsCoordinates);

            System.out.println(formattedLatitude);
            return formattedLatitude;
        }

        return "";
    }

    private static String formatLatitude(DMSCoordinates dmsCoordinates) {
        double latitudeDegrees = dmsCoordinates.getLatDegrees();
        double latitudeMinutes = dmsCoordinates.getLatMinutes();
        double latitudeSeconds = dmsCoordinates.getLatSeconds();

        return String.format("%d°%d'%f\"", (int) latitudeDegrees, (int) latitudeMinutes, latitudeSeconds);
    }

}
