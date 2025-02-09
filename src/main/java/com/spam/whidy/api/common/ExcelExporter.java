package com.spam.whidy.api.common;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

public class ExcelExporter {

    public static <T> void exportToExcel(List<T> data, Class<T> clazz, OutputStream out) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("장소 등록 sheet");
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = createHeaderCellStyle(workbook);
        createHeaderRow(clazz, headerRow, headerStyle);

        int rowIndex = 1;
        for (T item : data) {
            Row row = sheet.createRow(rowIndex++);
            populateDataRow(item, clazz, row);
        }

        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 750);
        }

        workbook.write(out);
        workbook.close();
    }

    // 헤더 컬럼 만들기
    private static <T> void createHeaderRow(Class<T> clazz, Row headerRow, CellStyle headerStyle) {
        int colIndex = 0;
        for (Field field : clazz.getDeclaredFields()) {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (annotation != null) {
                Cell cell = headerRow.createCell(colIndex++);
                cell.setCellValue(annotation.name());
                cell.setCellStyle(headerStyle);
            }
        }
    }

    // 데이터 행 채우기
    private static <T> void populateDataRow(T item, Class<T> clazz, Row row) throws Exception {
        int colIndex = 0;
        for (Field field : clazz.getDeclaredFields()) {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (annotation != null) {
                field.setAccessible(true);
                Object value = field.get(item);
                Cell cell = row.createCell(colIndex++);

                if (value != null) {
                    if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            }
        }
    }

    // 헤더 컬럼 양식
    private static CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 배경 색
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 텍스트 정렬
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 폰트
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        return style;
    }
}