package com.example.coordinatesconverter.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CoordinatesFileService {

    List<String> processCoordinatesFile(MultipartFile file) throws IOException;

    ResponseEntity<byte[]> processExcelFile(MultipartFile file) throws IOException;

    ResponseEntity<byte[]> processWordFile(MultipartFile file) throws IOException;

    ResponseEntity<byte[]> processXmlFile(MultipartFile file) throws IOException;

    ResponseEntity<byte[]> processTextFile(MultipartFile file, String conversionType) throws IOException;

}
