package com.example.coordinatesconverter.controller;

import com.example.coordinatesconverter.service.CoordinatesFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coordinates/process-file")
public class CoordinatesFileController {

    private final CoordinatesFileService coordinatesFileService;

    @PostMapping("/process-file")
    public List<String> processCoordinatesFile(@RequestPart MultipartFile file) throws IOException {
        return coordinatesFileService.processCoordinatesFile(file);
    }

    @PostMapping("/excel")
    public ResponseEntity<byte[]> processExcelFile(@RequestPart MultipartFile file) throws IOException {
        return coordinatesFileService.processExcelFile(file);
    }

    @PostMapping("/word")
    public ResponseEntity<byte[]> processWordFile(@RequestPart MultipartFile file,
                                                  @RequestHeader(value = "Conversion-Type",
                                                          defaultValue = "")
                                                  String conversionType)
            throws IOException {
        return coordinatesFileService.processWordFile(file, conversionType);
    }

    @PostMapping("/xml")
    public ResponseEntity<byte[]> processXmlFile(@RequestPart MultipartFile file) throws IOException {
        return coordinatesFileService.processXmlFile(file);
    }
    @PostMapping("/text")
    public ResponseEntity<byte[]> processTextFile(@RequestPart MultipartFile file,
                                                  @RequestHeader(value = "Conversion-Type",
                                                          defaultValue = "")
                                                  String conversionType)
            throws IOException {
        return coordinatesFileService.processTextFile(file, conversionType);
    }

}