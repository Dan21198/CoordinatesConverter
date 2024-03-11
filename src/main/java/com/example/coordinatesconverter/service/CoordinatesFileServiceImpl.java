package com.example.coordinatesconverter.service;

import com.example.coordinatesconverter.util.CoordinateExcelNormalizer;
import com.example.coordinatesconverter.util.CoordinateNormalizer;
import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CoordinatesFileServiceImpl implements CoordinatesFileService {

    private final CoordinateNormalizer coordinateNormalizer;
    private final CoordinateExcelNormalizer coordinateExcelNormalizer;
    private final CoordinatesFileConversionService coordinatesFileConversionService;
    private final CoordinatesExcelConversionService coordinatesExcelConversionService;

    @Override
    public ResponseEntity<byte[]> processExcelFile(MultipartFile file, String conversionType) throws IOException {
        byte[] fileContent = createProcessedExcelFile(file, conversionType);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("filename", "processed_excel_file.xlsx");
        headers.setContentLength(fileContent.length);

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }

    private byte[] createProcessedExcelFile(MultipartFile file, String conversionType) throws IOException {
        Workbook originalWorkbook = WorkbookFactory.create(file.getInputStream());
        Workbook processedWorkbook = new XSSFWorkbook();

        for (int i = 0; i < originalWorkbook.getNumberOfSheets(); i++) {
            Sheet originalSheet = originalWorkbook.getSheetAt(i);
            List<String> standardizedCoordinates = coordinateExcelNormalizer.normalizeSheet(originalSheet);
            if (conversionType != null && !conversionType.isEmpty()) {
                standardizedCoordinates = coordinatesExcelConversionService.convertCoordinates(conversionType
                        , standardizedCoordinates);
            }
            Sheet processedSheet = processedWorkbook.createSheet(originalSheet.getSheetName());

            for (int j = 0; j < standardizedCoordinates.size(); j++) {
                Row row = processedSheet.createRow(j);
                String[] coordinates = standardizedCoordinates.get(j).split("\t");

                for (int k = 0; k < coordinates.length; k++) {
                    Cell cell = row.createCell(k);
                    String value = coordinates[k];
                    if (j == 0) {
                        cell.setCellValue(value);
                    } else {
                        try {
                            double numericValue = Double.parseDouble(value.trim().replace(',', '.'));
                            cell.setCellValue(numericValue);
                        } catch (NumberFormatException e) {
                            cell.setCellValue(value);
                        }
                    }
                }
            }
        }

        originalWorkbook.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        processedWorkbook.write(outputStream);
        processedWorkbook.close();

        return outputStream.toByteArray();
    }

    @Override
    public ResponseEntity<byte[]> processWordFile(MultipartFile file, String conversionType) throws IOException {
        try (XWPFDocument document = new XWPFDocument(file.getInputStream())) {
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            List<String> standardizedCoordinates = new ArrayList<>();
            for (XWPFParagraph paragraph : paragraphs) {
                String text = paragraph.getText();
                String standardizedText = coordinateNormalizer.normalizeCoordinatesText(text);
                if (conversionType != null && !conversionType.isEmpty() && !standardizedText.isEmpty()) {
                    standardizedText = coordinatesFileConversionService.convertCoordinates(conversionType, standardizedText);
                }
                standardizedCoordinates.add(standardizedText);
            }

            XWPFDocument newDocument = new XWPFDocument();
            for (String text : standardizedCoordinates) {
                newDocument.createParagraph().createRun().setText(text);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            newDocument.write(outputStream);
            byte[] documentBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("filename", "standardized_document.docx");

            return new ResponseEntity<>(documentBytes, headers, HttpStatus.OK);
        }
    }
    @Override
    public ResponseEntity<byte[]> processTextFile(MultipartFile file, String conversionType) throws IOException {
        List<String> processedLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String processedLine = coordinateNormalizer.normalizeCoordinatesText(line);
                    if (conversionType == null || conversionType.isEmpty()) {
                        processedLines.add(processedLine);
                    } else {
                        String convertedLine = coordinatesFileConversionService.convertCoordinates(conversionType, processedLine);
                        processedLines.add(convertedLine);
                    }
                }
            }
        }

        String processedText = String.join("\n", processedLines);
        byte[] processedContent = processedText.getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("filename", "processed_text_file.txt");
        headers.setContentLength(processedContent.length);

        return new ResponseEntity<>(processedContent, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> processCsvFile(MultipartFile file, String conversionType) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet");

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .build();

        CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream(), StandardCharsets.ISO_8859_1))
                .withCSVParser(parser)
                .build();

        String[] nextLine;
        int rowNum = 0;
        while (true) {
            try {
                if ((nextLine = csvReader.readNext()) == null) break;
            } catch (CsvValidationException e) {
                throw new RuntimeException(e);
            }
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (String field : nextLine) {
                Cell cell = row.createCell(colNum++);
                field = field.replace(',', '.');
                cell.setCellValue(field);
            }
        }

        List<String> standardizedCoordinates = coordinateExcelNormalizer.normalizeSheet(sheet);
        if (conversionType != null && !conversionType.isEmpty()) {
            standardizedCoordinates = coordinatesExcelConversionService.convertCoordinates(conversionType
                    , standardizedCoordinates);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        CSVWriter csvWriter = new CSVWriter(
                new OutputStreamWriter(outputStream, StandardCharsets.ISO_8859_1),
                ';',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END
        );
        System.out.println(standardizedCoordinates);
        for (String coordinates : standardizedCoordinates) {
            String[] coordinatePair = coordinates.trim().split("\t");
            csvWriter.writeNext(coordinatePair);
        }
        csvWriter.close();

        byte[] fileContent = outputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("filename", "processed_csv_file.csv");
        headers.setContentLength(fileContent.length);

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }

}
