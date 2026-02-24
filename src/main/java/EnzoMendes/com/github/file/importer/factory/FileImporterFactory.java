package EnzoMendes.com.github.file.importer.factory;

import EnzoMendes.com.github.data.dto.PersonDTO;
import EnzoMendes.com.github.exceptions.BadRequestException;
import EnzoMendes.com.github.file.importer.contract.FileImporter;
import EnzoMendes.com.github.file.importer.impl.CsvImporter;
import EnzoMendes.com.github.file.importer.impl.XlsxImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class FileImporterFactory implements FileImporter {

    private static final Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);

    private final ApplicationContext context;

    public FileImporterFactory(ApplicationContext context){
        this.context = context;
    }

    public FileImporter getImporter(String fileName) throws Exception {
        if(fileName.endsWith(".xlsx")) {
            return context.getBean(XlsxImporter.class);
        } else if (fileName.endsWith(".csv")) {
            return context.getBean(CsvImporter.class);
        } else {
            throw new BadRequestException("Invalid file format.");
        }
    }

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {
        return List.of();
    }
}
