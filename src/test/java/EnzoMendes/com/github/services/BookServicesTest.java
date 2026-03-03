package EnzoMendes.com.github.services;

import EnzoMendes.com.github.data.dto.BookDTO;
import EnzoMendes.com.github.exceptions.RequiredObjectIsNullException;
import EnzoMendes.com.github.model.Book;
import EnzoMendes.com.github.repositories.BookRepository;
import EnzoMendes.com.github.unitytests.mapper.mocks.MockBook;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookServicesTest {

    MockBook input;

    @InjectMocks
    private BookService service;

    @Mock
    BookRepository repository;

    @BeforeEach
    void setUp(){
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void findById(){
        Book book = input.mockEntity(1);
        book.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        var result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());

        assertBookLinks(result);

        assertEquals("Book title1", result.getTitle());
        assertEquals("Book author1", result.getAuthor());
        assertEquals(1.00, result.getPrice());
        assertEquals("Book launch date1", result.getLaunchDate());
    }

    @Test
    void create(){
        Book book = input.mockEntity(1);
        Book persisted = book;
        persisted.setId(1L);

        BookDTO dto = input.mockDTO(1);

        when(repository.save(book)).thenReturn(persisted);

        var result = service.create(dto);

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(repository).save((captor.capture()));

        Book saved = captor.getValue();

        assertEquals(dto.getAuthor(), saved.getAuthor());
        assertEquals(dto.getTitle(), saved.getTitle());
        assertEquals(dto.getLaunchDate(), saved.getLaunchDate());
        assertEquals(dto.getPrice(), saved.getPrice());

        assertNotNull(result);
        assertEquals(1L, result.getId());

        assertBookLinks(result);
    }

    @Test
    void testCreateWithNullBook() {
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
        Book book = input.mockEntity(1);
        Book persisted = book;
        persisted.setId(1L);

        BookDTO dto = input.mockDTO(1);

        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(repository.save(book)).thenReturn(persisted);

        var result = service.update(dto);

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(repository).save(captor.capture());

        Book saved = captor.getValue();

        assertEquals(dto.getAuthor(), saved.getAuthor());
        assertEquals(dto.getTitle(), saved.getTitle());
        assertEquals(dto.getLaunchDate(), saved.getLaunchDate());
        assertEquals(dto.getPrice(), saved.getPrice());

        assertNotNull(result);
        assertEquals(1L, result.getId());

        assertBookLinks(result);

    }

    @Test
    void testUpdateWithNullBook(){
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
        Book book = input.mockEntity(1);
        book.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        service.delete(1L);
        verify(repository, times((1))).findById(anyLong());
        verify(repository, times((1))).delete(any(Book.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @Disabled("REASON: Still in development")
    void findAll(){
        List<Book> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        List<BookDTO> books = new ArrayList<>();

        assertNotNull(books);
        assertEquals(14, books.size());

        var bookOne = books.get(1);
        assertNotNull(bookOne);
        assertNotNull(bookOne.getId());

        assertBookLinks(bookOne);


        assertEquals("Book title1", bookOne.getTitle());
        assertEquals("Book author1", bookOne.getAuthor());
        assertEquals(1.00, bookOne.getPrice());
        assertEquals("Book launch date1", bookOne.getLaunchDate());

        var bookFour = books.get(4);
        assertNotNull(bookFour);
        assertNotNull(bookFour.getId());

        assertBookLinks(bookFour);


        assertEquals("Book title4", bookFour.getTitle());
        assertEquals("Book author4", bookFour.getAuthor());
        assertEquals(4.00, bookFour.getPrice());
        assertEquals("Book launch date4", bookFour.getLaunchDate());

        var bookSeven = books.get(7);
        assertNotNull(bookSeven);
        assertNotNull(bookSeven.getId());

        assertBookLinks(bookSeven);


        assertEquals("Book title7", bookSeven.getTitle());
        assertEquals("Book author7", bookSeven.getAuthor());
        assertEquals(7.00, bookSeven.getPrice());
        assertEquals("Book launch date7", bookSeven.getLaunchDate());
    }



    //helpers
    private void assertBookLinks(BookDTO result) {
        assertNotNull(result.getLinks());

        assertTrue(hasLink(result, "self", "/api/book/v1/" + result.getId(), "GET"), "self failed");
        assertTrue(hasLink(result, "findAll", "/api/book/v1", "GET"), "findAll failed");
        assertTrue(hasLink(result, "create", "/api/book/v1", "POST"), "post failed");
        assertTrue(hasLink(result, "update", "/api/book/v1", "PUT"), "put failed");
        assertTrue(hasLink(result, "delete", "/api/book/v1/" + result.getId(), "DELETE"), "delete failed");
    }

    private boolean hasLink(BookDTO dto, String rel, String path, String method){
        return dto.getLinks().stream()
                .anyMatch(link ->
                        link.getRel().value().equals(rel) &&
                                link.getHref().endsWith(path) &&
                                link.getType().equals(method));
    }
}
