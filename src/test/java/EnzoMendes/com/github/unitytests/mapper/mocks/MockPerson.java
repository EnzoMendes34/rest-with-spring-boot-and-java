package EnzoMendes.com.github.unitytests.mapper.mocks;

import EnzoMendes.com.github.data.dto.PersonDTO;
import EnzoMendes.com.github.model.Person;

import java.util.ArrayList;
import java.util.List;

public class MockPerson {

    public Person mockEntity(){ return mockEntity(0); }

    public PersonDTO mockDTO(){ return mockDTO(0); }


    public List<Person> mockEntityList(){
        List<Person> persons = new ArrayList<>();
        for(int i = 0; i < 14; i++){
            persons.add(mockEntity(i));
        }
        return persons;
    }

    public List<PersonDTO> mockDTOList(){
        List<PersonDTO> persons = new ArrayList<>();
        for(int i = 0; i < 14; i++){
            persons.add(mockDTO(i));
        }
        return persons;
    }

    public Person mockEntity(Integer number){
        Person person = new Person();
        person.setId(number.longValue());
        person.setAddress("Address test" + number);
        person.setFirstName("First name test" + number);
        person.setLastName("Last name test" + number);
        person.setGender(((number % 2)==0) ? "Male" : "Female");

        return person;
    }

    public PersonDTO mockDTO(Integer number){
        PersonDTO person = new PersonDTO();
        person.setId(number.longValue());
        person.setAddress("Address test" + number);
        person.setFirstName("First name test" + number);
        person.setLastName("Last name test" + number);
        person.setGender(((number % 2)==0) ? "Male" : "Female");

        return person;
    }
}
