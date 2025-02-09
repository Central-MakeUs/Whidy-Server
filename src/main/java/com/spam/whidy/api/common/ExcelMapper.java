package com.spam.whidy.api.common;
import org.apache.poi.ss.usermodel.*;
import java.lang.reflect.Field;
import java.util.*;

public class ExcelMapper {

    public static <T> List<T> mapExcelToObjects(Sheet sheet, Class<T> clazz) throws Exception {
        Map<String, Integer> columnMap = createColumnMap(sheet.getRow(0));
        List<T> result = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // 첫 번째 행(헤더) 스킵
            if (isRowEmpty(row)) break;
            T instance = createInstanceFromRow(row, clazz, columnMap);
            result.add(instance);
        }

        return result;
    }

    private static Map<String, Integer> createColumnMap(Row headerRow) {
        Map<String, Integer> columnMap = new HashMap<>();
        for (Cell cell : headerRow) {
            columnMap.put(cell.getStringCellValue().trim(), cell.getColumnIndex());
        }
        return columnMap;
    }

    private static <T> T createInstanceFromRow(Row row, Class<T> clazz, Map<String, Integer> columnMap) throws Exception {
        T instance = clazz.getDeclaredConstructor().newInstance();

        for (Field field : clazz.getDeclaredFields()) {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (annotation == null) continue;

            String columnName = annotation.name();
            Integer columnIndex = columnMap.get(columnName);
            if (columnIndex == null) continue;

            field.setAccessible(true);
            field.set(instance, parseCellValue(row.getCell(columnIndex), field.getType()));
        }

        return instance;
    }

    private static boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    // 셀 데이터를 필드 타입에 맞게 변환
    private static Object parseCellValue(Cell cell, Class<?> fieldType) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> fieldType == Boolean.class || fieldType == boolean.class
                    ? Boolean.parseBoolean(cell.getStringCellValue())
                    : cell.getStringCellValue();
            case NUMERIC -> fieldType == Double.class || fieldType == double.class
                    ? cell.getNumericCellValue()
                    : fieldType == Integer.class || fieldType == int.class
                    ? (int) cell.getNumericCellValue()
                    : cell.getNumericCellValue();
            case BOOLEAN -> cell.getBooleanCellValue();
            default -> null;
        };
    }
}