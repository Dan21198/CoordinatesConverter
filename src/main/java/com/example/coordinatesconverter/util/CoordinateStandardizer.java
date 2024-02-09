package com.example.coordinatesconverter.util;

import org.springframework.stereotype.Component;

@Component
public class CoordinateStandardizer {

    public String standardizeCoordinatesText(String text) {

        // Add  ° before N and S in specific case of N x.x E x.x
        text = text.replaceAll("(\\b[NS])\\s+", "°$1 ");

        // Add ° before N, S, E, W in DD
        text = text.replaceAll("(\\d+(\\.\\d+)?)\\s*°?\\s*([NSWE])", "$1°$3");

        // Add " before N, S, E, W in DMS
        text = text.replaceAll("((\\d+°)(\\d+'\\d+(\\.\\d+)?\")[NSWE])", "$1,");

        // Deletes spaces
        text = text.replaceAll("\\s+", "");

        // Replace “ with "
        text = text.replaceAll("“", "\"");

        // Replace ´ with '
        text = text.replaceAll("´", "'");

        // Replace ´´ with "
        text = text.replaceAll("´´", "\"");

        // Replace '' with "
        text = text.replaceAll("''", "\"");

        // Replace multiple spaces, commas, or semicolons with a single comma
        text = text.replaceAll("[,;\\s]+", ",");

        // Remove commas, semicolons, or spaces at the beginning or end of the text
        text = text.replaceAll("^[,;\\s]+|[;\\s]+$", "");

        // Replace a single '-' not followed by another '-' with ','
        text = text.replaceAll("(?<![-,])-(?!-)|(?<!,)\\s*-\\s*(?!-)", ",");

        // Replace multiple consecutive '-' with ',-' without affecting single '--'
        text = text.replaceAll("(?<!-)-(?!-)|--(?!-)", ",-");

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

        // Handle the case where there is a dot after the direction (N, S, E, W) before longitude
        text = text.replaceAll("([NSWE])\\.,(\\d)", "$1,$2");

        // Handle the case where there is a dot after the direction (N, S, E, W) before longitude
        text = text.replaceAll("([NSWE])\\.(\\d)", "$1,$2");

        // Handle the case where °E is missing
        text = text.replaceAll("(\\d+\\.\\d+),(\\d+\\.\\d+)°E", "$1°N,$2°E");

        // Handle the case where °E nad °N are swapped
        text = text.replaceAll("(\\d+\\.\\d+)°E,(\\d+\\.\\d+)°N", "$2°N,$1°E");

        // Handle the case where °N is missing
        text = text.replaceAll("(\\d+\\.\\d+°[NSWE]?),([\\d.]++)(?!°E|°W)", "$1,$2°E");

        text = text.replaceAll("(\\d+\\.\\d+°),(\\d+\\.\\d+°E)", "$1N,$2");

        // Handle the case where °N is missing
        text = text.replaceAll("(\\d+\\.\\d+°[NSWE]?),(E|W)?(\\d+\\.\\d+)", "$1,$3°E");

        // Handle the case where both latitude and longitude are missing directional indicators
        text = text.replaceAll("(\\d+\\.\\d+),(\\d+\\.\\d+)", "$1°N,$2°E");

        // Handle the case where °N is missing
        text = text.replaceAll("([NS])(\\d+\\.\\d+),([EW])(\\d+\\.\\d+)", "$2°$1,$4°$3");

        // Handle the case where both latitude and longitude are missing directional indicators
        text = text.replaceAll("(N)?(\\d+\\.\\d+)°(?:N|S)?, (\\d+\\.\\d+)°(?:E|W)?\n", "$2°$1, $4°$5");

        // Handle case with N, and E, are before number
        text = text.replaceAll("N,(\\d+\\.\\d+),E,(\\d+\\.\\d+)","$1°N,$2°E");

        // Add missing , after N and S
        text = text.replaceAll("((\\d+).(\\d+)(°[NS]?))((\\d+).(\\d+)(°[EW]?))","$1,$5");

        // Replace , symbols with . in decimal number
        text = text.replaceAll("(\\d+),(\\d+)(°[NS]?),(\\d+),(\\d+)(°[EW]?)", "$1.$2$3,$4.$5$6");

        // Handle °Nx.x°Ex.x -> x.x°Nx.x°E
        text = text.replaceAll("°([NSWE])(\\d+\\.\\d+)", "$2°$1,");

        //deletes letters without °
        text = text.replaceAll("(?<![°'\"])[NSWE]", "");

        //deletes , before text
        text = text.replaceAll("(?<![^,]),", "");

        //deletes ° before number
        text = text.replaceAll("^°", "");

        //deletes ° after "
        text = text.replaceAll("\"°", "\"");

        // Handle the case of duplicated indicators
        text = text.replaceAll("(°[NSWE])(°[NSWE])", "$2");

        // Regex to remove the last character if it's not 'E' or 'W'
        text = text.replaceAll("[^EW]$", "");

        return text;
    }
}
