package com.example.coordinatesconverter.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.*;


@Component
public class CoordinateExcelNormalizer {
    public List<String> normalizeExcelCoordinates(MultipartFile file) throws IOException {
        List<String> normalizedCoordinates = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(file.getInputStream());

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            normalizedCoordinates.addAll(normalizeSheet(sheet));
        }

        workbook.close();
        return normalizedCoordinates;
    }

    private List<String> normalizeSheet(Sheet sheet) {
        List<String> normalizedCoordinates = new ArrayList<>();
        int textColumnCount = countTextColumns(sheet);

        Row firstRow = sheet.getRow(0);
        String firstRowData = normalizeFirstRow(firstRow);
        if (firstRowData != null) {
            normalizedCoordinates.add(firstRowData);
        }

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String normalizedCoordinate = null;
            switch (textColumnCount) {
                case 2:
                    normalizedCoordinate = normalizeDD(row);
                    break;
                case 4:
                    normalizedCoordinate = normalizeDM(row);
                    break;
                case 6:
                    normalizedCoordinate = normalizeDMS(row);
                    break;
                default:
                    break;
            }
            if (normalizedCoordinate != null) {
                normalizedCoordinates.add(normalizedCoordinate);
            }
        }

        return normalizedCoordinates;
    }


    private int countTextColumns(Sheet sheet) {
        Row firstRow = sheet.getRow(0);
        if (firstRow == null) {
            return 0;
        }

        int count = 0;
        for (int i = 0; i < firstRow.getLastCellNum(); i++) {
            Cell cell = firstRow.getCell(i);
            if (cell != null && cell.getCellType() == CellType.STRING) {
                count++;
            }
        }
        return count;
    }

    private String normalizeFirstRow(Row firstRow) {
        if (firstRow != null) {
            StringBuilder sb = new StringBuilder();
            Iterator<Cell> cellIterator = firstRow.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                sb.append(cell.toString());
                if (cellIterator.hasNext()) {
                    sb.append("\t");
                }
            }
            return sb.toString();
        }
        return null;
    }

    private String normalizeDD(Row row) {
        StringBuilder normalizedCoordinates = new StringBuilder();

        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                if (cell.getCellType() == CellType.NUMERIC) {
                    normalizedCoordinates.append(cell.getNumericCellValue());
                } else if (cell.getCellType() == CellType.STRING) {
                    String cellValue = cell.getStringCellValue();
                    String decimalNumber = cellValue.replaceAll("[^\\d.]", "");
                    normalizedCoordinates.append(decimalNumber);
                }
            }
            if (i < row.getLastCellNum() - 1) {
                normalizedCoordinates.append("\t");
            }
        }
        normalizedCoordinates.append("\n");

        return normalizedCoordinates.toString();
    }


    private String normalizeDM(Row row) {
        StringBuilder normalizedCoordinates = new StringBuilder();

        Cell degreeCell = row.getCell(0);
        Cell decimalCell = row.getCell(1);

        if (degreeCell != null && degreeCell.getCellType() == CellType.NUMERIC &&
                decimalCell != null && decimalCell.getCellType() == CellType.NUMERIC) {
            double degrees = degreeCell.getNumericCellValue();
            double decimal = decimalCell.getNumericCellValue();

            normalizedCoordinates.append(degrees).append(" ").append(decimal);
        } else {
            normalizedCoordinates.append("Invalid format");
        }

        return normalizedCoordinates.toString();
    }

    private String normalizeDMS(Row row) {
        boolean isFirstRow = true;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= 6; i += 2) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() == CellType.STRING) {
                String cellValue = cell.getStringCellValue();
                if (cellValue.matches("^\\d+(\\.\\d+)?$")) {
                    if (isFirstRow) {
                        stringBuilder.append(cellValue).append(" ");
                    } else {
                        String[] parts = cellValue.split("\\.");
                        if (parts.length == 2 && parts[0].matches("^\\d+$") && parts[1].matches("^\\d+$")) {
                            stringBuilder.append(parts[0]).append(" ").append(parts[1]).append(".");
                        } else {
                            stringBuilder.append("Invalid format").append(" ");
                        }
                    }
                } else {
                    stringBuilder.append("Invalid format").append(" ");
                }
            }
        }
        return stringBuilder.toString().trim();
    }
}
