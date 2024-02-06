package com.example.coordinatesconverter.controller;

import com.example.coordinatesconverter.service.CoordinatesFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
}