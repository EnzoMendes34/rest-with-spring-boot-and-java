package EnzoMendes.com.github.services;

import EnzoMendes.com.github.controllers.BookController;
import EnzoMendes.com.github.data.dto.BookDTO;
import EnzoMendes.com.github.exceptions.RequiredObjectIsNullException;
import EnzoMendes.com.github.exceptions.ResourceNotFoundException;
import EnzoMendes.com.github.model.Book;
import EnzoMendes.com.github.repositories.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;


import static EnzoMendes.com.github.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    @Autowired
    BookRepository repository;

    private final PagedResourcesAssembler<BookDTO> assembler;

    public BookService(PagedResourcesAssembler<BookDTO> assembler){  this.assembler = assembler;}

    private Logger logger = LoggerFactory.getLogger(BookService.class.getName());

    public BookDTO findById(Long id){
        logger.info("Finding the Book");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No books found for this Id"));

        var dto = parseObject(entity, BookDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    public PagedModel<EntityModel<BookDTO>> findAll(Pageable pageable){
        logger.info("Searching all books");

        var pageableList = repository.findAll(pageable);

        var pageableListWitLinks = pageableList.map(book ->{

            var bookDTO = parseObject(book, BookDTO.class);
            addHateoasLinks(bookDTO);

            return bookDTO;
        });

        Link findAllLinks = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class).findAll(pageable.getPageNumber(),
                pageable.getPageSize(),
                String.valueOf(pageable.getSort())))
                .withSelfRel();

        return assembler.toModel(pageableListWitLinks, findAllLinks);
    }

    public BookDTO create(BookDTO book){
        if(book == null) throw new RequiredObjectIsNullException();

        logger.info("Creating and Saving a new book");

        var entity = parseObject(book, Book.class);

        var dto = parseObject(repository.save(entity), BookDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    public BookDTO update(BookDTO book){
        if(book == null) throw new RequiredObjectIsNullException();

        logger.info("Updating a book");

        Book entity =  repository.findById(book.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No books found for the given ID"));

        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());

        var dto = parseObject(repository.save(entity), BookDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    public void delete(Long id){
        logger.info("Deleting book");

        Book entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No books found for the given ID"));

        repository.delete(entity);
    }



    //helper
    private void addHateoasLinks(BookDTO dto){
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
