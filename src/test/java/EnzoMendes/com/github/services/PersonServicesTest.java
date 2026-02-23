package EnzoMendes.com.github.services;

import EnzoMendes.com.github.data.dto.PersonDTO;
import EnzoMendes.com.github.exceptions.RequiredObjectIsNullException;
import EnzoMendes.com.github.model.Person;
import EnzoMendes.com.github.repositories.PersonRepository;
import EnzoMendes.com.github.unitytests.mapper.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {


    MockPerson input;

    @InjectMocks
    private PersonServices service;

    @Mock
    PersonRepository repository;


    @BeforeEach
    void setUp(){
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById(){
        Person person = input.mockEntity(1);
        person.setId(1L);
         when(repository.findById(1L)).thenReturn(Optional.of(person));

         var result = service.findById(1L);

         assertNotNull(result);
         assertNotNull(result.getId());

         assertPersonLinks(result);


        assertEquals("Address test1", result.getAddress());
        assertEquals("First name test1", result.getFirstName());
        assertEquals("Last name test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void create(){
        Person person = input.mockEntity(1);
        Person persisted = person;
        persisted.setId(1L);

        PersonDTO dto = input.mockDTO(1);

        when(repository.save(person)).thenReturn(persisted);

        var result = service.create(dto);

        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(repository).save(captor.capture());

        Person saved = captor.getValue();

        assertEquals(dto.getFirstName(), saved.getFirstName());
        assertEquals(dto.getLastName(), saved.getLastName());
        assertEquals(dto.getAddress(), saved.getAddress());
        assertEquals(dto.getGender(), saved.getGender());

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(dto.getFirstName(), result.getFirstName());

        assertPersonLinks(result);
    }

    @Test
    void testCreateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                    service.create(null);
                });

        String expectedMessage = "It's not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update(){
        Person person = input.mockEntity(1);
        Person persisted = person;
        persisted.setId(1L);

        PersonDTO dto = input.mockDTO(1);

        when(repository.findById(1L)).thenReturn(Optional.of(person));
        when(repository.save(person)).thenReturn(persisted);

        var result = service.update(dto);

        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(repository).save(captor.capture());

        Person saved = captor.getValue();

        assertEquals(dto.getFirstName(), saved.getFirstName());
        assertEquals(dto.getLastName(), saved.getLastName());
        assertEquals(dto.getAddress(), saved.getAddress());
        assertEquals(dto.getGender(), saved.getGender());

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(dto.getFirstName(), result.getFirstName());

        assertPersonLinks(result);
    }

    @Test
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                    service.update(null);
                });

        String expectedMessage = "It's not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete(){
        Person person = input.mockEntity(1);
        person.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(person));

        service.delete(1L);
        verify(repository, times((1))).findById(anyLong());
        verify(repository, times((1))).delete(any(Person.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @Disabled("REASON: still under Development")
    void findAll(){
        List<Person> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        List<PersonDTO> people = new ArrayList<>();

        assertNotNull(people);
        assertEquals(14, people.size());

        var personOne = people.get(1);
        assertNotNull(personOne);
        assertNotNull(personOne.getId());

        assertPersonLinks(personOne);


        assertEquals("Address test1", personOne.getAddress());
        assertEquals("First name test1", personOne.getFirstName());
        assertEquals("Last name test1", personOne.getLastName());
        assertEquals("Female", personOne.getGender());

        var personFour = people.get(4);
        assertNotNull(personFour);
        assertNotNull(personFour.getId());

        assertPersonLinks(personOne);


        assertEquals("Address test4", personFour.getAddress());
        assertEquals("First name test4", personFour.getFirstName());
        assertEquals("Last name test4", personFour.getLastName());
        assertEquals("Male", personFour.getGender());

        var personSeven = people.get(7);
        assertNotNull(personSeven);
        assertNotNull(personSeven.getId());

        assertPersonLinks(personSeven);


        assertEquals("Address test7", personSeven.getAddress());
        assertEquals("First name test7", personSeven.getFirstName());
        assertEquals("Last name test7", personSeven.getLastName());
        assertEquals("Female", personSeven.getGender());
    }


    //helpers
    private void assertPersonLinks(PersonDTO result) {
        assertNotNull(result.getLinks());

        assertTrue(hasLink(result, "self", "/api/person/v1/" + result.getId(), "GET"), "self failed");
        assertTrue(hasLink(result, "findAll", "/api/person/v1", "GET"), "findAll failed");
        assertTrue(hasLink(result, "create", "/api/person/v1", "POST"), "post failed");
        assertTrue(hasLink(result, "update", "/api/person/v1", "PUT"), "put failed");
        assertTrue(hasLink(result, "delete", "/api/person/v1/" + result.getId(), "DELETE"), "delete failed");
    }

    private boolean hasLink(PersonDTO dto, String rel, String path, String method){
        return dto.getLinks().stream()
                .anyMatch(link ->
                        link.getRel().value().equals(rel) &&
                        link.getHref().endsWith(path) &&
                        link.getType().equals(method));
    }
}
