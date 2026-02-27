package EnzoMendes.com.github.file.exporter.impl;

import EnzoMendes.com.github.data.dto.PersonDTO;
import EnzoMendes.com.github.exceptions.ExportFileException;
import EnzoMendes.com.github.file.exporter.contract.PersonExporter;
import EnzoMendes.com.github.services.QRCodeService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PdfExporter implements PersonExporter {

    private QRCodeService service;

    private PdfExporter(QRCodeService service){ this.service = service; }

    @Override
    public Resource exportPeople(List<PersonDTO> people) throws IOException, JRException {
        InputStream stream = getClass().getResourceAsStream("/templates/people.jrxml");
        if ( stream == null){
            throw new ExportFileException("Template file not found: /templates/people.jrxml");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(stream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);

        Map<String, Object> parameters = new HashMap();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters ,dataSource);

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            return new ByteArrayResource(outputStream.toByteArray());
        } catch (Error e){
            throw new JRException("Error generating report, try again later");
        }
    }

    @Override
    public Resource exportPerson(PersonDTO person) throws Exception {
        InputStream mainTemplateStream = getClass().getResourceAsStream("/templates/person.jrxml");
        if ( mainTemplateStream == null){
            throw new ExportFileException("Template file not found: /templates/person.jrxml");
        }

        InputStream subReportStream = getClass().getResourceAsStream("/templates/books.jrxml");
        if ( subReportStream == null){
            throw new ExportFileException("Template file not found: /templates/person.jrxml");
        }

        JasperReport mainReport = JasperCompileManager.compileReport(mainTemplateStream);
        JasperReport subReport = JasperCompileManager.compileReport(subReportStream);

        InputStream qrCodeStream = service.generateQRCode(person.getProfileUrl(), 200, 200);

        JRBeanCollectionDataSource subReportDataSource = new JRBeanCollectionDataSource(Collections.singletonList(person.getBooks()));

        String path = getClass().getResource("/templates/books.jasper").getPath();

        Map<String, Object> parameters = new HashMap();
        parameters.put("SUB_REPORT_DATA_SOURCE",subReportDataSource);
        parameters.put("SUB_REPORT_DIR",path);
        parameters.put("BOOK_SUB_REPORT",subReport);
        parameters.put("QR_CODE_IMAGE",qrCodeStream);

        JRBeanCollectionDataSource mainDataSource = new JRBeanCollectionDataSource(Collections.singletonList(person));

        JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport,parameters ,mainDataSource);

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            return new ByteArrayResource(outputStream.toByteArray());
        } catch (Error e){
            throw new JRException("Error generating report, try again later");
        }
    
    }
}
