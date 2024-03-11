package com.example.coordinatesconverter.service.normalizer;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoordinateNormalizerServiceImpl implements CoordinateNormalizerService {

    private final CoordinateTextNormalizer coordinateNormalizer;
    private final CoordinateExcelNormalizer coordinateExcelNormalizer;

    @Override
    public String normalizeCoordinatesText(String text) {
        return coordinateNormalizer.normalizeCoordinatesText(text);
    }

    @Override
    public List<String> normalizeExcelSheet(Sheet sheet) {
        return coordinateExcelNormalizer.normalizeSheet(sheet);
    }
}
