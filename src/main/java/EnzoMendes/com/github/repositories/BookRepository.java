package EnzoMendes.com.github.repositories;

import EnzoMendes.com.github.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {}
