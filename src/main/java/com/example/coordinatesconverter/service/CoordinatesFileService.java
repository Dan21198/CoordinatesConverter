package com.example.coordinatesconverter.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CoordinatesFileService {

    ResponseEntity<byte[]> processExcelFile(MultipartFile file, String conversionType) throws IOException;

    ResponseEntity<byte[]> processWordFile(MultipartFile file, String conversionType) throws IOException;

    ResponseEntity<byte[]> processTextFile(MultipartFile file, String conversionType) throws IOException;

    ResponseEntity<byte[]> processCsvFile(MultipartFile file, String conversionType) throws IOException;

}
