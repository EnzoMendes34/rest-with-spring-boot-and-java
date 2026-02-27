package EnzoMendes.com.github.services;

import EnzoMendes.com.github.controllers.PersonController;
import EnzoMendes.com.github.data.dto.PersonDTO;
import EnzoMendes.com.github.exceptions.*;

import static EnzoMendes.com.github.mapper.ObjectMapper.parseObject;

import EnzoMendes.com.github.file.exporter.contract.PersonExporter;
import EnzoMendes.com.github.file.exporter.factory.FileExporterFactory;
import EnzoMendes.com.github.file.importer.contract.FileImporter;
import EnzoMendes.com.github.file.importer.factory.FileImporterFactory;
import EnzoMendes.com.github.model.Person;
import EnzoMendes.com.github.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;


@Service
public class PersonServices {

    @Autowired
    PersonRepository repository;

    FileImporterFactory importer;

    FileExporterFactory exporter;

    private final PagedResourcesAssembler<PersonDTO> assembler;

    public PersonServices(PagedResourcesAssembler<PersonDTO> assembler, FileImporterFactory importer, FileExporterFactory exporter){
        this.assembler = assembler;
        this.importer = importer;
        this.exporter = exporter;
    }

    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    public PersonDTO findById(Long id) {
        logger.info("Finding one Person");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));

        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    public Resource exportPage(Pageable pageable, String acceptHeader){
        logger.info("Exporting selected People page.");

        var people = repository.findAll(pageable).map(person -> parseObject(person, PersonDTO.class)).getContent();

        try {
            PersonExporter exporter = this.exporter.getExporter(acceptHeader);
            return exporter.exportPeople(people);

        } catch (Exception e) {
            throw new ExportFileException("Error during file export. Error: " + e );
        }
    };

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable){
        logger.info("Searching all People");

        var pageableList = repository.findAll(pageable);

        return buildPagedModel(pageable, pageableList);
    };

    public PagedModel<EntityModel<PersonDTO>> findPeopleByName(String firstName, Pageable pageable){
        logger.info("Searching people with the given first name");

        var pageableList = repository.findPeopleByName(firstName, pageable);

        return buildPagedModel(pageable, pageableList);
    };

    public PersonDTO create(PersonDTO person){
        if(person == null) throw new RequiredObjectIsNullException();

        logger.info("Creating a person");
        var entity = parseObject(person, Person.class);

        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public List<PersonDTO> createMultiplePeople(MultipartFile file) {
        logger.info("Importing and Creating all People");

        if(file.isEmpty()) throw new BadRequestException("Please set a valid file");

        try(InputStream inputStream = file.getInputStream()){
            String fileName = Optional.ofNullable(file.getOriginalFilename())
                    .orElseThrow(() -> new BadRequestException("File name cannot be null"));
            FileImporter importer = this.importer.getImporter(fileName);

            List<Person> entities = importer.importFile(inputStream).stream().map(dto ->
                    repository.save(parseObject(dto, Person.class))).toList();

            return entities.stream().map(entity -> {
                var dto = parseObject(entity, PersonDTO.class);
                addHateoasLinks(dto);
                return dto;
            }).toList();
        } catch (Exception e) {
            throw new FileStorageException("Error while processing file");
        }
    };

    public PersonDTO update(PersonDTO person){
        if(person == null) throw new RequiredObjectIsNullException();
        logger.info("Updating a person");

        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No People found for the given ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    @Transactional
    public PersonDTO disablePerson(Long id){
        logger.info("Setting this person as disabled!");

         repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No People found for the given ID"));

         repository.disablePerson(id);

         var entity = repository.findById(id).get();

         var dto = parseObject(entity, PersonDTO.class);
         addHateoasLinks(dto);

         return dto;
    }

    public void delete(Long id){
        logger.info("Deleting a person!");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No People found for the given ID"));

        repository.delete(entity);
    }

    public Resource exportPerson(Long id, String acceptHeader) {
        logger.info("Exporting data of one Person");

        var person = repository.findById(id)
                .map(entity -> parseObject(entity, PersonDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));

        try {
            PersonExporter exporter = this.exporter.getExporter(acceptHeader);
            return exporter.exportPerson(person);
        } catch (Exception e) {
            throw new ExportFileException("Error during file export. Error: " + e );
        }
    }

    //helpers
    private void addHateoasLinks(PersonDTO dto){
        dto.add(linkTo(methodOn(PersonController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findPeopleByName("", 1, 12, "asc")).withRel("findPeopleByName").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).exportPage(1, 12, "asc", null)).withRel("exportPage").withType("GET").withTitle("Export people"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class)).slash("createMultiplePeople").withRel("createMultiplePeople").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }

    private PagedModel<EntityModel<PersonDTO>> buildPagedModel(Pageable pageable, Page<Person> people) {
        var pageableListWithLinks = people.map(person -> {
            PersonDTO personDTO = parseObject(person, PersonDTO.class);
            addHateoasLinks(personDTO);

            return personDTO;
        });

        Link findAllLinks = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class)
                        .findAll(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        String.valueOf(pageable.getSort())))
                .withSelfRel();
        return assembler.toModel(pageableListWithLinks, findAllLinks);
    }
}
