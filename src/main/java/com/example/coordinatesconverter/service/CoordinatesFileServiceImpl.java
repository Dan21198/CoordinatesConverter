package com.example.coordinatesconverter.service;

import com.example.coordinatesconverter.model.CoordinatesXML;
import com.example.coordinatesconverter.util.CoordinateNormalizer;
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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CoordinatesFileServiceImpl implements CoordinatesFileService {

    private final CoordinateNormalizer coordinateNormalizer;

    @Override
    public List<String> processCoordinatesFile(MultipartFile file) throws IOException {
        List<String> standardizedCoordinates = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            reader.lines().map(coordinateNormalizer::normalizeCoordinatesText).forEach(standardizedCoordinates::add);
        }
        return standardizedCoordinates;
    }

    @Override
    public ResponseEntity<byte[]> processExcelFile(MultipartFile file) throws IOException {

        List<String> standardizedCoordinates = processExcelContent(file);
        byte[] fileContent = getFileContent(standardizedCoordinates);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("filename", "processed_excel_file.xlsx");
        headers.setContentLength(fileContent.length);

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }

    private static byte[] getFileContent(List<String> standardizedCoordinates) throws IOException {
        Workbook processedWorkbook = new XSSFWorkbook();
        Sheet sheet = processedWorkbook.createSheet("Processed Coordinates");

        for (int i = 0; i < standardizedCoordinates.size(); i++) {
            Row row = sheet.createRow(i);
            Cell cell = row.createCell(0);
            cell.setCellValue(standardizedCoordinates.get(i));
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        processedWorkbook.write(outputStream);
        processedWorkbook.close();

        byte[] fileContent = outputStream.toByteArray();
        return fileContent;
    }

    private List<String> processExcelContent(MultipartFile file) throws IOException {
        List<String> standardizedCoordinates = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            while (sheetIterator.hasNext()) {
                Sheet sheet = sheetIterator.next();
                Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        if (cell.getCellType() == CellType.STRING) {
                            String cellValue = cell.getStringCellValue();
                            standardizedCoordinates.add(coordinateNormalizer.normalizeCoordinatesText(cellValue));
                        }
                    }
                }
            }
        }
        return standardizedCoordinates;
    }

    @Override
    public ResponseEntity<byte[]> processWordFile(MultipartFile file) throws IOException {
        try (XWPFDocument document = new XWPFDocument(file.getInputStream())) {
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            List<String> standardizedCoordinates = new ArrayList<>();
            for (XWPFParagraph paragraph : paragraphs) {
                String text = paragraph.getText();
                String standardizedText = coordinateNormalizer.normalizeCoordinatesText(text);
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
    public ResponseEntity<byte[]> processXmlFile(MultipartFile file) throws IOException {

        List<String> standardizedCoordinates = processXmlContent(file);
        String processedXmlContent = generateXmlContent(standardizedCoordinates);
        byte[] fileContent = processedXmlContent.getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setContentDispositionFormData("filename", "processed_xml_file.xml");
        headers.setContentLength(fileContent.length);

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }

    private List<String> processXmlContent(MultipartFile file) throws IOException {
        List<String> standardizedCoordinates = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            JAXBContext jaxbContext = JAXBContext.newInstance(CoordinatesXML.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            CoordinatesXML coordinatesXML = (CoordinatesXML) unmarshaller.unmarshal(inputStream);
            standardizedCoordinates.add(coordinateNormalizer.normalizeCoordinatesText(coordinatesXML.getLatitude()));
            standardizedCoordinates.add(coordinateNormalizer.normalizeCoordinatesText(coordinatesXML.getLongitude()));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return standardizedCoordinates;
    }

    private String generateXmlContent(List<String> standardizedCoordinates) {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<coordinates>\n");
        for (String coordinate : standardizedCoordinates) {
            xmlBuilder.append("<coordinate>").append(coordinate).append("</coordinate>\n");
        }
        xmlBuilder.append("</coordinates>");
        return xmlBuilder.toString();
    }

    @Override
    public ResponseEntity<byte[]> processTextFile(MultipartFile file) throws IOException {

        byte[] fileContent = file.getBytes();
        String text = new String(fileContent, StandardCharsets.UTF_8);
        String processedText = coordinateNormalizer.normalizeCoordinatesText(text);
        byte[] processedContent = processedText.getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("filename", "processed_text_file.txt");
        headers.setContentLength(processedContent.length);

        return new ResponseEntity<>(processedContent, headers, HttpStatus.OK);
    }


}
