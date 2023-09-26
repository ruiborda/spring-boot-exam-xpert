package com.example.expert.controller;

import com.example.expert.entity.Book;
import com.example.expert.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    Book bookL1;
    Book bookEntityL1;

    Long idNotExist = -1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookController = new BookController(bookService);
        bookL1 = new Book(1L, "The Great Gatsby", null);
        bookEntityL1 = new Book(1L, "The Great Gatsby", null);
    }

    public void assertBookEquals(Book book, Book bookAdapter){
        assertEquals(book.getId(), bookAdapter.getId());
        assertEquals(book.getTitle(), bookAdapter.getTitle());
    }

    @Test
    void getAllBooks() {
        when(bookService.findAll()).thenReturn(List.of(bookEntityL1));
        List<Book> books = bookController.getAllBooks().getBody();
        assertNotNull(books);
        assertEquals(1, books.size());
    }

    @Test
    void createBook() {
        when(bookService.save(bookL1)).thenReturn(bookEntityL1);
        Book bookAdapter = bookController.createBook(bookL1).getBody();
        assertNotNull(bookAdapter);
        assertBookEquals(bookL1, bookAdapter);
    }

    @Test
    void getBookById() {
        when(bookService.findById(bookL1.getId())).thenReturn(bookEntityL1);
        Book bookAdapter = bookController.getBookById(bookL1.getId()).getBody();
        assertNotNull(bookAdapter);
        assertBookEquals(bookL1, bookAdapter);
    }

    @Test
    void updateBook() {
        when(bookService.findById(bookL1.getId())).thenReturn(bookEntityL1);
        when(bookService.save(bookL1)).thenReturn(bookEntityL1);
        Book bookAdapter = bookController.updateBook(bookL1.getId(), bookL1).getBody();
        assertNotNull(bookAdapter);
        assertBookEquals(bookL1, bookAdapter);
    }

    @Test
    void deleteBookById() {
        when(bookService.findById(bookL1.getId())).thenReturn(bookEntityL1);
        bookController.deleteBookById(bookL1.getId());
        verify(bookService, times(1)).deleteById(bookL1.getId());
    }

    @Test
    void getBookByIdNotFound() {
        when(bookService.findById(idNotExist)).thenReturn(null);
        Book bookAdapter = bookController.getBookById(idNotExist).getBody();
        assertNull(bookAdapter);
    }
}
