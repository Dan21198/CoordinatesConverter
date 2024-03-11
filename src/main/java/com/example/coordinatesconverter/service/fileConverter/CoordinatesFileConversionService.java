package com.example.coordinatesconverter.service.fileConverter;

import java.util.List;

public interface CoordinatesFileConversionService {

    String convertTextCoordinates(String conversionType, String coordinates);

    List<String> convertExcelCoordinates(String conversionType, List<String> coordinates);

}