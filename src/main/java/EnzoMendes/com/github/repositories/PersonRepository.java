package EnzoMendes.com.github.repositories;

import EnzoMendes.com.github.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {}
