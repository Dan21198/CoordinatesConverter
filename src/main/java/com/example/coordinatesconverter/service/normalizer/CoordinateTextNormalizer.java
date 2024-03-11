package com.example.coordinatesconverter.service.normalizer;

import com.example.coordinatesconverter.util.NormalizerConverterHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CoordinateTextNormalizer {

    public String normalizeCoordinatesText(String text) {
        text = normalizeDD(text);
        text = normalizeDM(text);
        text = normalizeDMS(text);
        text = normalizeMixedCoordinates(text);
        text = replaceDecimalSymbol(text);
        return text;
    }

    private static String replaceDecimalSymbol(String text) {
        text = text.replaceAll("(?<=\\d),(?=\\d)", ".");
        return text;
    }

    private String normalizeDD(String text) {
        // Replace whitespace before North (N) or South (S) with degree symbol (°) and the direction
        text = text.replaceAll("(\\b[NS])\\s+", "°$1 ");

        // Add degree symbol ° between the numeric value and the direction (N/S/W/E)
        text = text.replaceAll("(\\d+(\\.\\d+)?)\\s*°?\\s*([NSWE])", "$1°$3");

        // Remove all whitespace
        text = text.replaceAll("\\s+", "");

        // Replace commas, semicolons, or multiple spaces with a single comma
        text = text.replaceAll("[,;]+", ",");

        // Replace specific quotation marks with standard double quotes
        text = text.replaceAll("“", "\"");
        text = text.replaceAll("´", "'");
        text = text.replaceAll("´´", "\"");
        text = text.replaceAll("''", "\"");

        // Trim leading and trailing commas, semicolons, or spaces
        text = text.replaceAll("^[,;]+|[,;]+$", "");

        // Replace multiple consecutive dashes with a single comma
        text = text.replaceAll("(?<!-)-(?!-)|--(?!-)", ",-");

        // Remove trailing comma after dash
        text = text.replaceAll("-,", "-");

        // Remove consecutive commas
        text = text.replaceAll(",,+", ",");

        // Remove trailing comma
        text = text.replaceAll(",$", "");

        // Remove consecutive commas
        text = text.replaceAll(",,+", ",");

        // Handle ., before longitude
        text = text.replaceAll("([NSWE])\\.,(\\d)", "$1,$2");

        // Handle . before longitude
        text = text.replaceAll("([NSWE])\\.(\\d)", "$1,$2");

        // Handle °E before Lon
        text = text.replaceAll("(\\d+\\.\\d+°N?),(E|°E)?(\\d+\\.\\d+)", "$1,$3");

        // Handle °W before Lon
        text = text.replaceAll("(\\d+\\.\\d+°S?),(W|°W)?(\\d+\\.\\d+)", "$1,$3");

        // Handle missing °N by adding °N and °E
        text = text.replaceAll("(\\d+\\.\\d+),(\\d+\\.\\d+)°E", "$1°N,$2°E");

        // Handle missing °S by adding °S and °W
        text = text.replaceAll("(\\d+\\.\\d+),(\\d+\\.\\d+)°W", "$1°S,$2°W");

        // Handle missing °E
        text = text.replaceAll("(\\d+\\.\\d+°N),(\\d+\\.\\d++)(?!E)", "$1,$2°E");

        // Handle missing °W
        text = text.replaceAll("(\\d+\\.\\d+°S),(\\d+\\.\\d++)(?!W)", "$1,$2°W");

        // Handle swapped °E and °N
        text = text.replaceAll("(\\d+\\.\\d+)°E,(\\d+\\.\\d+)°N", "$2°N,$1°E");

        // Handle swapped °W and °S
        text = text.replaceAll("(\\d+\\.\\d+)°W,(\\d+\\.\\d+)°S", "$2°S,$1°W");

        // Handle missing °N and °E by adding both
        text = text.replaceAll("(\\d+\\.\\d+),(\\d+\\.\\d+)", "$1°N,$2°E");

        // Handle format with direction before longitude
        text = text.replaceAll("([NS])(\\d+\\.\\d+),([EW])(\\d+\\.\\d+)", "$2°$1,$4°$3");

        // Handle format with missing direction indicators
        text = text.replaceAll("(N)?(\\d+\\.\\d+)°[NS]?, (\\d+\\.\\d+)°[EW]?\n", "$2°$1, $4°$5");

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

        // Handle direction before decimal number
        text = text.replaceAll("°([NSWE])(\\d+\\.\\d+)", "$2°$1,");

        // Remove direction indicators without °, ' or "
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
        // replace " for ' in DM format
        text = text.replaceAll("(\\d+°\\d+\\.\\d+)([NSWE],\\d+°\\d+\\.\\d+'[NSWE])", "$1',$3");
        text = text.replaceAll("(\\d+°\\d+\\.\\d+')([NSWE],\\d+°\\d+\\.\\d+)\"([NSWE])", "$1$2'$3");

        // Handle °E before Lon
        text = text.replaceAll("(\\d+°\\d+\\.\\d+'N?),(E|'E)?(\\d+°\\d+\\.\\d+)", "$1,$3");

        // Handle °W before Lon
        text = text.replaceAll("(\\d+°\\d+\\.\\d+'S?),(W|'W)?(\\d+°\\d+\\.\\d+)", "$1,$3");

        // Handle missing °N by adding °N and °E
        text = text.replaceAll("(\\d+°\\d+\\.\\d+),(\\d+°\\d+\\.\\d+)'E", "$1'N,$2'E");

        // Handle missing °S by adding °S and °W
        text = text.replaceAll("(\\d+°\\d+\\.\\d+),(\\d+°\\d+\\.\\d+)'W", "$1'S,$2'W");

        // Handle missing °E
        text = text.replaceAll("(\\d+°\\d+\\.\\d+'N),(\\d+°\\d+\\.\\d++')(?!E)", "$1,$2'E");

        // Handle missing °W
        text = text.replaceAll("(\\d+°\\d+\\.\\d+'S),(\\d+°\\d+\\.\\d++')(?!W)", "$1,$2'W");

        // Handle swapped °E and °N
        text = text.replaceAll("(\\d+°\\d+\\.\\d+)'E,(\\d+°\\d+\\.\\d+)'N", "$2'N,$1'E");

        // Handle swapped °W and °S
        text = text.replaceAll("(\\d+°\\d+\\.\\d+)'W,(\\d+°\\d+\\.\\d+)'S", "$2'S,$1'W");

        // Handle missing °N and °E by adding both
        text = text.replaceAll("(\\d+°\\d+\\.\\d+),(\\d+°\\d+\\.\\d+)", "$1'N,$2'E");

        // Handle missing °N by adding °N
        text = text.replaceAll("(\\d+°\\d+\\.\\d+'(?!N)),((\\d+°\\d+\\.\\d+'(?=E))E)", "$1N,$2");

        return text;
    }

    private String normalizeDMS(String text) {
        // Add " before N, S, E, W in DMS
        text = text.replaceAll("((\\d+°)(\\d+'\\d+(\\.\\d+)?\")[NSWE])", "$1,");

        // Handle x°x'x.xN -> x°x'x.x"N
        text = text.replaceAll("(\\d+°\\d+'\\d+\\.\\d+)([NWSE])", "$1\"$2");

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

        text = text.replaceAll("(\\d+\\.\\d+°),(\\d+\\.\\d+°E)", "$1N,$2");

        // Handle °E before Lon
        text = text.replaceAll("(\\d+°\\d+'\\d+\\.\\d+\"N?),(E|'E)?(\\d+°\\d+'\\d+\\.\\d+)", "$1,$3");

        // Handle °W before Lon
        text = text.replaceAll("(\\d+°\\d+'\\d+\\.\\d+\"S?),(W|'W)?(\\d+°\\d+'\\d+\\.\\d+)", "$1,$3");

        // Handle missing °N by adding °N and °E
        text = text.replaceAll("(\\d+°\\d+'\\d+\\.\\d+),(\\d+°\\d+'\\d+\\.\\d+)\"E", "$1\"N,$2\"E");

        // Handle missing °S by adding °S and °W
        text = text.replaceAll("(\\d+°\\d+'\\d+\\.\\d+),(\\d+°\\d+'\\d+\\.\\d+)\"W", "$1\"S,$2\"W");

        // Handle missing °E
        text = text.replaceAll("(\\d+°\\d+'\\d+\\.\\d+\"N),(\\d+°\\d+'\\d+\\.\\d++\")(?!E)", "$1,$2\"E");

        // Handle missing °W
        text = text.replaceAll("(\\d+°\\d+'\\d+\\.\\d+\"S),(\\d+°\\d+'\\d+\\.\\d++\")(?!W)", "$1,$2\"W");

        // Handle swapped °E and °N
        text = text.replaceAll("(\\d+°\\d+'\\d+\\.\\d+)\"E,(\\d+°\\d+'\\d+\\.\\d+)\"N", "$2\"N,$1\"E");

        // Handle swapped °W and °S
        text = text.replaceAll("(\\d+°\\d+'\\d+\\.\\d+)\"W,(\\d+°\\d+'\\d+\\.\\d+)\"S", "$2\"S,$1\"W");

        // Handle missing °N and °E by adding both
        text = text.replaceAll("(\\d+°\\d+'\\d+\\.\\d+),(\\d+°\\d+'\\d+\\.\\d+)", "$1\"N,$2\"E");

        // Handle missing °N by adding °N
        text = text.replaceAll("(\\d+°\\d+'\\d+\\.\\d+\"(?!N)),((\\d+°\\d+'\\d+\\.\\d+\"(?=E))E)", "$1N,$2");

        // Handle the case where °N is missing
        //text = text.replaceAll("(\\d+\\.\\d+°[NSWE]?),(E|W)?(\\d+\\.\\d+)", "$1,$3°E");

        // Handle the case where both latitude and longitude are missing directional indicators
        text = text.replaceAll("(\\d+\\.\\d+),(\\d+\\.\\d+)", "$1°N,$2°E");

        // Handle the case where both latitude and longitude are missing directional indicators
        text = text.replaceAll("(N)?(\\d+\\.\\d+)°[NS]?, (\\d+\\.\\d+)°[EW]?\n", "$2°$1, $4°$5");

        // Replace , symbols with . in decimal number in DMS
        text = text.replaceAll("(\\d+)(°\\d+'\\d+),(\\d+)([NS]?),(\\d+)(°\\d+'\\d+),(\\d+)([EW]?)",
                "$1$2.$3$4,$5$6.$7$8");

        // Add missing , after N and S in DMS
        text = text.replaceAll("(\\d+°\\d+'\\d+\\.\\d+\")([NS])(\\d+°\\d+'\\d+\\.\\d+\")([EW])"
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

        // Fix E° inside x°E°x'x.x"E
        text = text.replaceAll("(\\d+\\.\\d+°[NS]),(\\d+°)([EW]°)(\\d+)", "$1,$2$4");

        return text;
    }

    private static String normalizeMixedCoordinates(String text) {

        final Map<String, String> patternsToMethods = getPatternMap();

        StringBuilder result = new StringBuilder();
        int lastIndex = 0;

        for (Map.Entry<String, String> entry : patternsToMethods.entrySet()) {
            String regex = entry.getKey();
            String conversionMethod = entry.getValue();

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                String matchedCoordinate = matcher.group(2);
                String convertedCoordinate = convertCoordinate(conversionMethod, matchedCoordinate);
                result.append(text, lastIndex, matcher.start(2));
                result.append(convertedCoordinate);
                result.append(matcher.group(3));
                lastIndex = matcher.end(3);
            }
        }

        result.append(text, lastIndex, text.length());

        return result.toString();
    }

    private static Map<String, String> getPatternMap() {
        final Map<String, String> patternsToMethods = new HashMap<>();

        patternsToMethods.put("\\b(\\d+°\\d+'\\d+\\.\\d+\"[NS],(\\d+\\.\\d+))°([EW])\\b", "convertDDLonToDMSLon");
        patternsToMethods.put("\\b((\\d+\\.\\d+))°([NS]),(\\d+°\\d+'\\d+\\.\\d+)\\\"([EW])\\b", "convertDDLatToDMSLat");
        patternsToMethods.put("\\b((\\d+.\\d+))°([NS]),\\d+°\\d+.\\d+'([EW])\\b", "convertDDLatToDMLat");
        patternsToMethods.put("\\b\\d+°\\d+.\\d+'([NS]),(\\d+.\\d+)°([EW])\\b", "convertDDLonToDMLon");
        patternsToMethods.put("\\b((\\d+°\\d+.\\d+'))([NS]),\\d+°\\d+'\\d+\\.\\d+\"([EW])\\b", "convertDMLatToDMSLat");
        patternsToMethods.put("\\b\\d+°\\d+'\\d+\\.\\d+\"([NS]),(\\d+°\\d+.\\d+')([EW])\\b", "convertDMLonToDMSLon");

        return patternsToMethods;
    }

    private static String convertCoordinate(String conversionMethod, String coordinate) {
        return switch (conversionMethod) {
            case "convertDDLonToDMSLon" -> NormalizerConverterHelper.convertDDLonToDMSLon(coordinate);
            case "convertDDLatToDMSLat" -> NormalizerConverterHelper.convertDDLatToDMSLat(coordinate);
            case "convertDDLonToDMLon" -> NormalizerConverterHelper.convertDDToDMLon(coordinate);
            case "convertDDLatToDMLat" -> NormalizerConverterHelper.convertDDToDMLat(coordinate);
            case "convertDMLonToDMSLon" -> NormalizerConverterHelper.convertDMToDMSLon(coordinate);
            case "convertDMLatToDMSLat" -> NormalizerConverterHelper.convertDMToDMSLat(coordinate);
            default -> coordinate;
        };
    }

}
