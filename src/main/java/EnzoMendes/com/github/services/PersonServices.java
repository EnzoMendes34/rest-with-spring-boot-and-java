package EnzoMendes.com.github.services;

import EnzoMendes.com.github.model.Person;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    public Person findById(String id) {
        logger.info("Finding one Person");

        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Person 1");
        person.setLastName("Lastname");
        person.setAddress("Jundiaí SP");
        person.setGender("Male");

        return person;
    }

    public List<Person> findAll(){
        logger.info("Finding all People");
        List<Person> persons = new ArrayList<Person>();
        for(int i = 0; i < 8; i++){
            Person person = new Person();
            person = mockPerson(i);
            persons.add(person);
        }

        return persons;
    }

    public Person create(Person person){
        logger.info("Creating a person");


        return person;
    }

    public Person update(Person person){
        logger.info("Updating a person");

        return person;
    }

    public void delete(String id){
        logger.info("Deleting a person!");
        Person person = this.findById(id);
    }

    private Person mockPerson(int i) {
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("FirsName" + i);
        person.setLastName("Lastname" + i);
        person.setAddress("Address " + i);
        person.setGender("MockGender" + i);

        return person;
    }
}
