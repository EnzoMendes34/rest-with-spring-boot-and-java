package EnzoMendes.com.github.file.exporter.impl;

import EnzoMendes.com.github.data.dto.PersonDTO;
import EnzoMendes.com.github.exceptions.ExportFileException;
import EnzoMendes.com.github.file.exporter.contract.PersonExporter;
import net.sf.jasperreports.engine.JRException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class XlsxExporter implements PersonExporter {
    @Override
    public Resource exportPeople(List<PersonDTO> people) throws IOException {
        try(Workbook workBook = new XSSFWorkbook()){
            Sheet sheet =  workBook.createSheet("People");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "First Name", "Last Name", "Address", "Gender", "Enabled"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderCellStyle(workBook));
            }

            int rowIndex = 1;

            for (PersonDTO person : people) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(person.getId());
                row.createCell(1).setCellValue(person.getFirstName());
                row.createCell(2).setCellValue(person.getLastName());
                row.createCell(3).setCellValue(person.getAddress());
                row.createCell(4).setCellValue(person.getGender());
                row.createCell(5).setCellValue(
                        person.getEnabled() != null && person.getEnabled() ? "Active" : "Disabled"
                );
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workBook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (Error e){
            throw new ExportFileException("Error while trying to export file, please try again. ERROR: " + e.getMessage());
        }
    }

    private CellStyle createHeaderCellStyle(Workbook workBook) {
        CellStyle style = workBook.createCellStyle();
        Font font = workBook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);

        return style;
    }

    @Override
    public Resource exportPerson(PersonDTO person) throws IOException, JRException {
        return null;
    }
}
