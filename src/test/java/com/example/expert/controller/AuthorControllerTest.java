package com.example.expert.controller;

import com.example.expert.entity.Author;
import com.example.expert.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorControllerTest {

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    Author authorL1;
    Author authorEntityL1;

    Long idNotExist = -1L;

    Long idNull = null;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authorController = new AuthorController(authorService);
        authorL1 = new Author(1L, "John Doe",null);
        authorEntityL1 = new Author(1L, "John Doe",null);
    }

    public void assertAuthorEquals(Author author, Author authorAdapter){
        assertEquals(author.getId(), authorAdapter.getId());
        assertEquals(author.getName(), authorAdapter.getName());
    }

    @Test
    void getAllAuthors() {
        when(authorService.findAll()).thenReturn(List.of(authorEntityL1));
        List<Author> authors = authorController.getAllAuthors().getBody();
        assertNotNull(authors);
        assertEquals(1, authors.size());
    }

    @Test
    void createAuthor() {
        when(authorService.save(authorL1)).thenReturn(authorEntityL1);
        Author authorAdapter = authorController.createAuthor(authorL1).getBody();
        assertNotNull(authorAdapter);
        assertAuthorEquals(authorL1, authorAdapter);
    }

    @Test
    void getAuthorById() {
        when(authorService.findById(authorL1.getId())).thenReturn(authorEntityL1);
        Author authorAdapter = authorController.getAuthorById(authorL1.getId()).getBody();
        assertNotNull(authorAdapter);
        assertAuthorEquals(authorL1, authorAdapter);
    }

    @Test
    void updateAuthor() {
        when(authorService.findById(authorL1.getId())).thenReturn(authorEntityL1);
        when(authorService.save(authorL1)).thenReturn(authorEntityL1);
        Author authorAdapter = authorController.updateAuthor(authorL1.getId(), authorL1).getBody();
        assertNotNull(authorAdapter);
        assertAuthorEquals(authorL1, authorAdapter);
    }

    @Test
    void deleteAuthorById() {
        when(authorService.findById(authorL1.getId())).thenReturn(authorEntityL1);
        authorController.deleteAuthorById(authorL1.getId());
        verify(authorService, times(1)).deleteById(authorL1.getId());
    }

    @Test
    void getAuthorByIdNotFound() {
        when(authorService.findById(idNotExist)).thenReturn(null);
        Author authorAdapter = authorController.getAuthorById(idNotExist).getBody();
        assertNull(authorAdapter);
    }


    @Test
    void updateAuthorIdNull() {
        when(authorService.findById(idNull)).thenReturn(null);
        Author authorAdapter = authorController.updateAuthor(idNull, authorL1).getBody();
        assertNull(authorAdapter);
    }

    @Test
    void deleteAuthorByIdException() {
        when(authorService.findById(authorL1.getId())).thenReturn(authorEntityL1);
        doThrow(new RuntimeException()).when(authorService).deleteById(authorL1.getId());
        authorController.deleteAuthorById(authorL1.getId());
        verify(authorService, times(1)).deleteById(authorL1.getId());
    }


}
