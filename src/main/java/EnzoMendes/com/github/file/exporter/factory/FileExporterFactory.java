package EnzoMendes.com.github.file.exporter.factory;

import EnzoMendes.com.github.data.dto.PersonDTO;
import EnzoMendes.com.github.exceptions.BadRequestException;
import EnzoMendes.com.github.file.exporter.MediaTypes;
import EnzoMendes.com.github.file.exporter.contract.FileExporter;
import EnzoMendes.com.github.file.exporter.impl.CsvExporter;
import EnzoMendes.com.github.file.exporter.impl.XlsxExporter;
import EnzoMendes.com.github.file.importer.contract.FileImporter;
import EnzoMendes.com.github.file.importer.impl.CsvImporter;
import EnzoMendes.com.github.file.importer.impl.XlsxImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class FileExporterFactory implements FileExporter {

    private static final Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

    private final ApplicationContext context;

    public FileExporterFactory(ApplicationContext context){
        this.context = context;
    }

    public FileExporter getExporter(String acceptHeader) throws Exception {
        if(acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
            return context.getBean(XlsxExporter.class);
        } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)) {
            return context.getBean(CsvExporter.class);
        } else {
            throw new BadRequestException("Invalid file format.");
        }
    }

    @Override
    public Resource exportFile(List<PersonDTO> people)  {
        return null;
    }
}
