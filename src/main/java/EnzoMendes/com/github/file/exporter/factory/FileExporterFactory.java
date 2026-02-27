package EnzoMendes.com.github.file.exporter.factory;

import EnzoMendes.com.github.data.dto.PersonDTO;
import EnzoMendes.com.github.exceptions.BadRequestException;
import EnzoMendes.com.github.file.exporter.MediaTypes;
import EnzoMendes.com.github.file.exporter.contract.PersonExporter;
import EnzoMendes.com.github.file.exporter.impl.CsvExporter;
import EnzoMendes.com.github.file.exporter.impl.PdfExporter;
import EnzoMendes.com.github.file.exporter.impl.XlsxExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileExporterFactory implements PersonExporter {

    private static final Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

    private final ApplicationContext context;

    public FileExporterFactory(ApplicationContext context){
        this.context = context;
    }

    public PersonExporter getExporter(String acceptHeader) throws Exception {
        if(acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
            return context.getBean(XlsxExporter.class);
        } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)) {
            return context.getBean(CsvExporter.class);
        } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_PDF_VALUE)) {
            return context.getBean(PdfExporter.class);
        } else {
            throw new BadRequestException("Invalid file format.");
        }
    }

    @Override
    public Resource exportPeople(List<PersonDTO> people)  {
        return null;
    }

    @Override
    public Resource exportPerson(PersonDTO person) throws Exception {
        return null;
    }
}
