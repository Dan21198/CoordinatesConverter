package com.example.coordinatesconverter.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.*;


@Component
public class CoordinateExcelNormalizer {

    public List<String> normalizeSheet(Sheet sheet) {
        List<String> normalizedCoordinates = new ArrayList<>();
        int textColumnCount = countTextColumns(sheet);

        Row firstRow = sheet.getRow(0);
        if (firstRow != null) {
            StringBuilder firstRowData = new StringBuilder();
            for (int cellNum = 0; cellNum < firstRow.getLastCellNum(); cellNum++) {
                Cell cell = firstRow.getCell(cellNum);
                if (cell != null) {
                    switch (cell.getCellType()) {
                        case STRING:
                            firstRowData.append(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            firstRowData.append(cell.getNumericCellValue());
                            break;

                    }
                    if (cellNum < firstRow.getLastCellNum() - 1) {
                        firstRowData.append("\t");
                    }
                }
            }
            normalizedCoordinates.add(firstRowData.toString());
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

    private String normalizeDD(Row row) {
        StringBuilder normalizedCoordinates = new StringBuilder();
        Pattern dmsPattern = Pattern.compile("(\\d+)°(\\d+)'(\\d+(\\.\\d+)?)\"?");
        Pattern dmPattern = Pattern.compile("(\\d+)°(\\d+(\\.\\d+)?)'");

        Cell firstCell = row.getCell(0);
        if (firstCell != null && firstCell.getCellType() == CellType.STRING) {
            String firstCellValue = firstCell.getStringCellValue();
            if (firstCellValue.contains("E") || firstCellValue.contains("S")) {
                Cell secondCell = row.getCell(1);
                if (secondCell != null) {
                    row.getCell(0).setCellValue(secondCell.getStringCellValue());
                    secondCell.setCellValue(firstCellValue);
                }
            }
        }

        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                switch (cell.getCellType()) {
                    case NUMERIC:
                        normalizedCoordinates.append(cell.getNumericCellValue());
                        break;
                    case STRING:
                        String cellValue = cell.getStringCellValue();
                        Matcher dmsMatcher = dmsPattern.matcher(cellValue);
                        Matcher dmMatcher = dmPattern.matcher(cellValue);
                        if (dmsMatcher.find()) {
                            String matchedLongitude = dmsMatcher.group();
                            double ddValueLon = Double.parseDouble(NormalizerConverterHelper
                                    .convertDMSToDDLon(matchedLongitude));
                            normalizedCoordinates.append(ddValueLon);
                        } else if (dmMatcher.find()) {
                            String matchedLongitude = dmMatcher.group();
                            double ddValueLon = Double.parseDouble(NormalizerConverterHelper
                                    .convertDMToDDLon(matchedLongitude));
                            normalizedCoordinates.append(ddValueLon);
                        } else {
                            String decimalNumber = cellValue.replaceAll("[^\\d.]", "");
                            normalizedCoordinates.append(decimalNumber);
                        }
                        break;
                    default:
                        break;
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
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                String cellValue = "";

                if (cell.getCellType() == CellType.NUMERIC) {
                    double numericValue = cell.getNumericCellValue();
                    cellValue = Double.toString(numericValue);
                } else if (cell.getCellType() == CellType.STRING) {
                    cellValue = cell.getStringCellValue();
                    try {
                        double numericValue = Double.parseDouble(cellValue);
                        cellValue = Double.toString(numericValue);
                    } catch (NumberFormatException e) {
                        cellValue = cellValue.replaceAll("[^\\d.]", "");
                    }
                }

                stringBuilder.append(cellValue);
            }
            if (i < row.getLastCellNum() - 1) {
                stringBuilder.append("\t");
            }
        }

        return stringBuilder.toString().trim();
    }

    private String normalizeDMS(Row row) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                String cellValue = "";
                if (cell.getCellType() == CellType.NUMERIC) {
                    double numericValue = cell.getNumericCellValue();
                    if (i == 2 || i == 5) {
                        cellValue = String.format("%.5f", numericValue);
                    } else {
                        cellValue = Integer.toString((int) numericValue);
                    }
                } else if (cell.getCellType() == CellType.STRING) {
                    cellValue = cell.getStringCellValue();
                    if (i == 2 || i == 5) {
                        cellValue = cellValue.replaceAll("(\\d+[,.]\\d+)\\W\\w", "$1");
                        cellValue = cellValue.replaceAll("(\\d+[,.]\\d+)[A-Z^\\W\\s]", "$1");
                    } else {
                        cellValue = cellValue.replaceAll("(\\d+)[,.]\\d+", "$1");
                        cellValue = cellValue.replaceAll("[^\\d]", "");
                    }
                }

                stringBuilder.append(cellValue);
            }
            if (i < row.getLastCellNum() - 1) {
                stringBuilder.append("\t");
            }
        }

        return stringBuilder.toString().trim();
    }


}
