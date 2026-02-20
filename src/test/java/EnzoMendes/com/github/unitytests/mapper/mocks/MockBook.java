package EnzoMendes.com.github.unitytests.mapper.mocks;

import EnzoMendes.com.github.data.dto.BookDTO;
import EnzoMendes.com.github.model.Book;

import java.util.ArrayList;
import java.util.List;

public class MockBook {

    public Book mockEntity(){ return mockEntity(0);}

    public BookDTO mockDTO(){return mockDTO(0);}

    public List<Book> mockEntityList(){
        List<Book> books = new ArrayList<>();
        for(int i =0; i < 14; i++){
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookDTO> mockDTOList(){
        List<BookDTO> books = new ArrayList<>();
        for(int i=0; i < 14; i++){
            books.add(mockDTO(i));
        }
        return books;
    }

    public Book mockEntity(Integer number){
        Book book = new Book();
        book.setId(number.longValue());
        book.setTitle("Book title"+ number);
        book.setAuthor("Book author"+ number);
        book.setPrice(number.doubleValue());
        book.setLaunchDate("Book launch date"+ number);

        return book;
    }

    public BookDTO mockDTO(Integer number){
        BookDTO book = new BookDTO();
        book.setId(number.longValue());
        book.setTitle("Book title"+ number);
        book.setAuthor("Book author"+ number);
        book.setPrice(number.doubleValue());
        book.setLaunchDate("Book launch date"+ number);

        return book;
    }
}
