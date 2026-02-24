package EnzoMendes.com.github.file.importer.contract;

import EnzoMendes.com.github.data.dto.PersonDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream) throws Exception;
}
