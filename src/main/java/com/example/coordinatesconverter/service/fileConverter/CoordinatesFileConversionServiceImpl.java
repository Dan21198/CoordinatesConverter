package com.example.coordinatesconverter.service.fileConverter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoordinatesFileConversionServiceImpl implements CoordinatesFileConversionService{
    private final CoordinatesTextConversionService textConversionService;
    private final CoordinatesExcelConversionService excelConversionService;

    @Override
    public String convertTextCoordinates(String conversionType, String coordinates) {
        return textConversionService.convertCoordinates(conversionType, coordinates);
    }

    @Override
    public List<String> convertExcelCoordinates(String conversionType,  List<String> coordinates) {
        return excelConversionService.convertCoordinates(conversionType, coordinates);
    }
}
