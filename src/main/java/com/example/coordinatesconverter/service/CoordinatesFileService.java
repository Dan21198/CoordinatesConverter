package com.example.coordinatesconverter.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CoordinatesFileService {
    //List<String> processCoordinatesFile(String filePath) throws IOException;
    List<String> processCoordinatesFile(MultipartFile file) throws IOException;

}
