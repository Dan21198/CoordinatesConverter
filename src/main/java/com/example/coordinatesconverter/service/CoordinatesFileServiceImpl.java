package com.example.coordinatesconverter.service;

import com.example.coordinatesconverter.util.CoordinateStandardizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CoordinatesFileServiceImpl implements CoordinatesFileService {

    private final CoordinateStandardizer coordinateStandardizer;

    @Override
    public List<String> processCoordinatesFile(MultipartFile file) throws IOException {
        List<String> standardizedCoordinates = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            reader.lines().map(coordinateStandardizer::standardizeCoordinatesText).forEach(standardizedCoordinates::add);
        }
        return standardizedCoordinates;
    }


}
