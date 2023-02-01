package ru.danil.springcourse.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.danil.springcourse.model.Book;
import ru.danil.springcourse.model.Person;
import ru.danil.springcourse.repositories.BookRepository;
import ru.danil.springcourse.repositories.PeopleRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BookService(BookRepository bookRepository, PeopleRepository peopleRepository) {
        this.bookRepository = bookRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear)
            return bookRepository.findAll(Sort.by("year"));
        else
            return bookRepository.findAll();
    }

    public Book findForOne(int id) {
        Optional<Book> foundPerson = bookRepository.findById(id);
        return foundPerson.orElse(null);
    }

    public List<Book> searchByTitle(String text) {
        return bookRepository.findByTitleStartingWith(text);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updateBook) {
        updateBook.setId(id);
        updateBook.setOwner(updateBook.getOwner());
        bookRepository.save(updateBook);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    public Person getBookOwner(int id) {

        return bookRepository.findById(id).map(Book::getOwner).orElse(null);
    }


    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        Hibernate.initialize(person.get().getBookList());

        person.get().getBookList().forEach(book -> {
            long diffInMillies = Math.abs(book.getCreateAt().getTime() - new Date().getTime());
            if (diffInMillies > 864000000)
                book.setProsroshka(true);
        });

        return person.get().getBookList();
    }

    @Transactional
    public void dropBookBack(int id) {
        Book updateBook = bookRepository.getReferenceById(id);
        updateBook.setOwner(null);
        updateBook.setCreateAt(null);
    }

    @Transactional
    public void appointForPerson(int id, Person selectedPerson) {
        Book updateBook = bookRepository.getOne(id);
        updateBook.setOwner(selectedPerson);
        updateBook.setCreateAt(new Date());
        bookRepository.save(updateBook);
    }

    public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear)
            return bookRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        else
            return bookRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

}
