package com.example.coordinatesconverter.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CoordinatesFileServiceImpl implements CoordinatesFileService {

    @Override
    public List<String> processCoordinatesFile(MultipartFile file) throws IOException {
        List<String> standardizedCoordinates = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String standardizedLine = standardizeCoordinatesText(line);
                standardizedCoordinates.add(standardizedLine);
            }
        }

        return standardizedCoordinates;
    }

    private String standardizeCoordinatesText(String text) {
        // Replace N, S, E, W symbols with °
        text = text.replaceAll("(\\d+(\\.\\d+)?)\\s*°?\\s*([NSWE])", "$1°$3");

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

        // Regex to remove the last character if it's not 'E' or 'W'
        //text = text.replaceAll("[^EW]$", "");

        return text;
    }


    private static String adjustCoordinates(String coordinates) {
        // Regex pattern to match the cases
        Pattern pattern = Pattern.compile("(\\d+\\.\\d*)°([NS]),?\\s*(\\d+\\.\\d*)°([EW])?");

        // Matcher to find matches in the input string
        Matcher matcher = pattern.matcher(coordinates);

        // StringBuilder to build the result
        StringBuilder result = new StringBuilder();

        // Iterate through matches and adjust the coordinates
        while (matcher.find()) {
            // Append the latitude part
            result.append(matcher.group(1)).append("°").append(matcher.group(2)).append(",");

            // If longitude and its direction are present, append them
            if (matcher.group(3) != null && matcher.group(4) != null) {
                result.append(matcher.group(3)).append("°").append(matcher.group(4));
            }
        }

        return result.toString();
    }

}
