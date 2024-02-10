package com.example.coordinatesconverter.service;

import com.example.coordinatesconverter.util.CoordinateNormalizer;
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

    private final CoordinateNormalizer coordinateNormalizer;

    @Override
    public List<String> processCoordinatesFile(MultipartFile file) throws IOException {
        List<String> standardizedCoordinates = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            reader.lines().map(coordinateNormalizer::normalizeCoordinatesText).forEach(standardizedCoordinates::add);
        }
        return standardizedCoordinates;
    }

}
