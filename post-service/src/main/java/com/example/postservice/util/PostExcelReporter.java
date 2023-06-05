package com.example.postservice.util;

import com.example.postservice.entity.Post;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostExcelReporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Post> postList;
//    private final Long DAY = (long) (1000*60*60*24);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");

    public PostExcelReporter(List<Post> postList) {
        this.postList = postList;
        workbook = new XSSFWorkbook();
    }

    private void createSheet(String name){
//        Date from = new Date((new Date().getTime()) - DAY);
//        Date to = new Date();
        sheet = workbook.createSheet(name);
    }

    private void writeHeaderLine() {
        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Post ID", style);
        createCell(row, 1, "Creator ID", style);
        createCell(row, 2, "Status Content", style);
        createCell(row, 3, "Attachment URL", style);
        createCell(row, 4, "Created Time", style);
        createCell(row, 5, "Updated Time", style);
        createCell(row, 6, "Comments Count", style);
        createCell(row, 7, "Reactions Count", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Long) {
            cell.setCellValue((Long) value);
        }else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        }else if (value instanceof Date) {
            cell.setCellValue(dateFormat.format(value));
        }else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Post post : postList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

//            createCell(row, columnCount++, post.getId(), style);

            createCell(row, columnCount++, post.getPostId(), style);
            createCell(row, columnCount++, post.getCreatorId(), style);
            createCell(row, columnCount++, post.getStatusContent(), style);
            createCell(row, columnCount++, post.getAttachmentUrl(), style);
            createCell(row, columnCount++, post.getCreatedTime(), style);
            createCell(row, columnCount++, post.getUpdatedTime(), style);
            createCell(row, columnCount++, post.getComments().size(), style);
            createCell(row, columnCount++, post.getPostReactions().size(), style);
        }
    }

    public void export(HttpServletResponse response, String name) throws IOException {
        createSheet(name);
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}
