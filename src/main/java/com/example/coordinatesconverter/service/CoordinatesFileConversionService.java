package com.example.coordinatesconverter.service;

public interface CoordinatesFileConversionService {

    String convertCoordinates(String conversionType, String coordinates);

    String convertExcelCoordinates(String conversionType, String coordinates);

}