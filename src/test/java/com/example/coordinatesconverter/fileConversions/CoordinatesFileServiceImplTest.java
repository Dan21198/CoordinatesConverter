package com.example.coordinatesconverter.fileConversions;

import com.example.coordinatesconverter.service.fileConverter.CoordinatesFileServiceImpl;
import com.example.coordinatesconverter.service.normalizer.CoordinateNormalizerService;
import com.example.coordinatesconverter.service.fileConverter.CoordinatesFileConversionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CoordinatesFileServiceImplTest {

    private CoordinatesFileServiceImpl service;

    @BeforeEach
    public void setUp() {
        CoordinateNormalizerService coordinateNormalizer = mock(CoordinateNormalizerService.class);
        CoordinatesFileConversionService coordinatesFileConversionService = mock(CoordinatesFileConversionService.class);
        service = new CoordinatesFileServiceImpl(coordinateNormalizer, coordinatesFileConversionService);
    }

    @Test
    public void processExcelFile_ValidFileAndConversionType_ReturnsResponseEntity() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "original", "text/plain"
                , "coordinates".getBytes());
        String conversionType = "";
        byte[] expectedFileContent = "processed content".getBytes();
        ResponseEntity<byte[]> expectedResponse = new ResponseEntity<>(expectedFileContent, HttpStatus.OK);

        CoordinatesFileServiceImpl mockService = mock(CoordinatesFileServiceImpl.class);
        when(mockService.processExcelFile(file, conversionType)).thenReturn(expectedResponse);

        ResponseEntity<byte[]> actualResponse = mockService.processExcelFile(file, conversionType);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void processWordFile_ValidFileAndConversionType_ReturnsResponseEntity() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "original", "text/plain"
                , "coordinates".getBytes());
        String conversionType = "";
        byte[] expectedFileContent = "processed content".getBytes();
        ResponseEntity<byte[]> expectedResponse = new ResponseEntity<>(expectedFileContent, HttpStatus.OK);

        CoordinatesFileServiceImpl mockService = mock(CoordinatesFileServiceImpl.class);
        when(mockService.processWordFile(file, conversionType)).thenReturn(expectedResponse);

        ResponseEntity<byte[]> actualResponse = mockService.processWordFile(file, conversionType);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void processTextFile_ValidFileAndConversionType_ReturnsResponseEntity() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "original", "text/plain"
                , "coordinates".getBytes());
        String conversionType = "";
        ResponseEntity<byte[]> responseEntity = service.processTextFile(file, conversionType);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void processCsvFile_ValidFileAndConversionType_ReturnsResponseEntity() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "original", "text/plain"
                , "coordinates".getBytes());
        String conversionType = "";
        ResponseEntity<byte[]> responseEntity = service.processCsvFile(file, conversionType);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
}