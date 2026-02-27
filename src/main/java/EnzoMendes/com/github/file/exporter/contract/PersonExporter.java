package EnzoMendes.com.github.file.exporter.contract;

import EnzoMendes.com.github.data.dto.PersonDTO;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface PersonExporter {

    Resource exportPeople(List<PersonDTO> people) throws IOException, JRException;
    Resource exportPerson(PersonDTO person) throws Exception;
}
