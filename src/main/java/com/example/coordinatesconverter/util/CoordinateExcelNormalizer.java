package com.example.coordinatesconverter.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
        List<String> cellValues = new ArrayList<>();

        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                String cellValue = "";

                if ((i == 0 || i == 2) && row.getCell(i + 1) == null) {
                    if (cell.getCellType() == CellType.NUMERIC) {
                        cellValue = NormalizerConverterHelper
                                .convertExcelDDoDM(String.valueOf(cell.getNumericCellValue()));
                        splitDDToDMParts(cellValues, cellValue);
                    } else {
                        double numericValue = Double.parseDouble(cell.getStringCellValue()
                                .replaceAll("(\\d+[.,]\\d+).*", "$1")
                                .replaceAll("[^\\d.,]+", ""));
                        cellValue = NormalizerConverterHelper.convertExcelDDoDM(String.valueOf(numericValue));
                        splitDDToDMParts(cellValues, cellValue);
                    }
                } else {

                    switch (cell.getCellType()) {
                        case NUMERIC:
                            double numericValue = cell.getNumericCellValue();
                            if (i == 0 || i == 2) {
                                cellValue = String.format("%d", (int) numericValue);
                            } else if (i == 1 || i == 3) {
                                cellValue = String.format("%.5f", numericValue);
                            }
                            break;
                        case STRING:
                            String stringValue = cell.getStringCellValue().replace(',', '.');
                            try {
                                numericValue = Double.parseDouble(stringValue);
                                if (i == 0 || i == 2) {
                                    cellValue = String.format("%d", (int) numericValue);
                                } else if (i == 1 || i == 3) {
                                    cellValue = String.format("%.5f", numericValue);
                                }
                            } catch (NumberFormatException e) {
                                cellValue = stringValue.replaceAll("(\\d+[.,]\\d+).*", "$1")
                                        .replaceAll("[^\\d.,]+", "");
                                try {
                                    if (i == 0 || i == 2) {
                                        cellValue = String.format("%d", (int) Double.parseDouble(cellValue));
                                    } else if (i == 1 || i == 3) {
                                        cellValue = String.format("%.5f", Double.parseDouble(cellValue));
                                    }
                                } catch (NumberFormatException ex) {
                                    cellValue = stringValue;
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    cellValues.add(cellValue);
                }
            }
        }
        return String.join("\t", cellValues).trim();
    }

    private void splitDDToDMParts(List<String> cellValues, String cellValue) {
        if (cellValue.contains("°")) {
            String[] parts = cellValue.split("°");
            cellValues.add(parts[0].trim());

            if (parts.length > 1) {
                String minutePart = parts[1].replaceAll("[^\\d,]+", "").trim();
                cellValues.add(minutePart);
            }
        } else {
            cellValues.add(cellValue);
        }
    }

    private String normalizeDMS(Row row) {
        List<String> cellValues = new ArrayList<>();

        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                String cellValue = "";
                switch (cell.getCellType()) {
                    case NUMERIC:
                        double numericValue = cell.getNumericCellValue();
                        if (i == 2 || i == 5) {
                            cellValue = String.format("%.5f", numericValue);
                        } else if ((i == 0 || i == 3) && row.getCell(i + 1) == null) {
                            cellValue = Double.toString(numericValue);
                            cellValue = cellValue.replaceAll("(\\d+[,.]\\d+).*", "$1");
                            cellValue = NormalizerConverterHelper.convertExcelDDToDMS(cellValue);
                        } else if ((i == 0 || i == 3) && row.getCell(i + 1) != null
                                && row.getCell(i + 2) == null) {
                            Cell nextCell = row.getCell(i + 1);
                            if (nextCell != null && nextCell.getCellType() == CellType.NUMERIC) {
                                double nextNumericValue = nextCell.getNumericCellValue();
                                cellValue = NormalizerConverterHelper.convertExcelDMToDMS(Double.toString(numericValue)
                                        , Double.toString(nextNumericValue));
                                i++;
                            }
                        } else {
                            cellValue = Integer.toString((int) numericValue);
                        }
                        break;
                    case STRING:
                        cellValue = cell.getStringCellValue();
                        if (i == 2 || i == 5) {
                            cellValue = cellValue.replaceAll("(\\d+[,.]\\d+)\\W\\w", "$1");
                            cellValue = cellValue.replaceAll("(\\d+[,.]\\d+)[A-Z^\\W\\s]", "$1");
                        } else if ((i == 0 || i == 3) && row.getCell(i + 1) == null) {
                            cellValue = cellValue.replaceAll("(\\d+[,.]\\d+).*", "$1");
                            cellValue = NormalizerConverterHelper.convertExcelDDToDMS(cellValue);
                        } else if ((i == 0 || i == 3) && row.getCell(i + 1) != null
                                && row.getCell(i + 2) == null) {
                            cellValue = cellValue.replaceAll("(\\d+).*", "$1");
                            Cell nextCell = row.getCell(i + 1);
                            if (nextCell != null && nextCell.getCellType() == CellType.STRING) {
                                String nextCellValue = nextCell.getStringCellValue();
                                nextCellValue = nextCellValue.replaceAll("(\\d+[,.]\\d+).*", "$1");
                                cellValue = NormalizerConverterHelper.convertExcelDMToDMS(cellValue, nextCellValue);
                                i++;
                            }
                        } else {
                            cellValue = cellValue.replaceAll("(\\d+)[,.]\\d+", "$1");
                            cellValue = cellValue.replaceAll("[^\\d]", "");
                        }
                        break;
                    default:
                        break;
                }

                String[] parts = cellValue.split("[°'\"]");
                for (String part : parts) {
                    if (!part.isEmpty()) {
                        cellValues.add(part.trim());
                    }
                }
            }
        }

        return String.join("\t", cellValues).trim();
    }

}
