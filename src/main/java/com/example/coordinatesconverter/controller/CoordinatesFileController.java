package com.example.coordinatesconverter.controller;

import com.example.coordinatesconverter.service.fileConverter.CoordinatesFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coordinates/process-file")
public class CoordinatesFileController {

    private final CoordinatesFileService coordinatesFileService;

    @PostMapping("/excel")
    public ResponseEntity<byte[]> processExcelFile(@RequestPart MultipartFile file,
                                                   @RequestHeader(value = "Conversion-Type"
                                                           , defaultValue = "")
                                                   String conversionType)
            throws IOException {
        return coordinatesFileService.processExcelFile(file, conversionType);
    }

    @PostMapping("/word")
    public ResponseEntity<byte[]> processWordFile(@RequestPart MultipartFile file,
                                                  @RequestHeader(value = "Conversion-Type",
                                                          defaultValue = "")
                                                  String conversionType)
            throws IOException {
        return coordinatesFileService.processWordFile(file, conversionType);
    }

    @PostMapping("/text")
    public ResponseEntity<byte[]> processTextFile(@RequestPart MultipartFile file,
                                                  @RequestHeader(value = "Conversion-Type"
                                                          , defaultValue = "")
                                                  String conversionType)
            throws IOException {
        return coordinatesFileService.processTextFile(file, conversionType);
    }

    @PostMapping("/csv")
    public ResponseEntity<byte[]> processCsvFile(@RequestPart MultipartFile file,
                                                 @RequestHeader(value = "Conversion-Type"
                                                         , defaultValue = "")
                                                 String conversionType)
            throws IOException {
        return coordinatesFileService.processCsvFile(file, conversionType);
    }

}