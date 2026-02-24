package EnzoMendes.com.github.file.exporter.contract;

import EnzoMendes.com.github.data.dto.PersonDTO;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface FileExporter {

    Resource exportFile(List<PersonDTO> people) throws IOException;
}
