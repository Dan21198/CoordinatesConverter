package com.example.coordinatesconverter.service.normalizer;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface CoordinateNormalizerService {
    String normalizeCoordinatesText(String text);
    List<String> normalizeExcelSheet(Sheet sheet);
}
