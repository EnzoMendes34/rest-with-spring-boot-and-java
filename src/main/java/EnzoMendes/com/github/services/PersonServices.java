package EnzoMendes.com.github.services;

import EnzoMendes.com.github.controllers.PersonController;
import EnzoMendes.com.github.data.dto.PersonDTO;
import EnzoMendes.com.github.exceptions.RequiredObjectIsNullException;
import EnzoMendes.com.github.exceptions.ResourceNotFoundException;
import static EnzoMendes.com.github.mapper.ObjectMapper.parseListObjects;
import static EnzoMendes.com.github.mapper.ObjectMapper.parseObject;
import EnzoMendes.com.github.model.Person;
import EnzoMendes.com.github.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PersonServices {

    @Autowired
    PersonRepository repository;

    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    public PersonDTO findById(Long id) {
        logger.info("Finding one Person");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));

        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    public List<PersonDTO> findAll(){
        logger.info("Searching all People");

        var people = parseListObjects(repository.findAll(), PersonDTO.class);
        people.forEach(this::addHateoasLinks);
        return people;
    };

    public PersonDTO create(PersonDTO person){
        if(person == null) throw new RequiredObjectIsNullException();

        logger.info("Creating a person");
        var entity = parseObject(person, Person.class);

        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

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

    //helper
    private void addHateoasLinks(PersonDTO dto){
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
