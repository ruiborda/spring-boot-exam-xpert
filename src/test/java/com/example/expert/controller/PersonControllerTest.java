package com.example.expert.controller;

import com.example.expert.entity.Person;
import com.example.expert.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonControllerTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    Person personL1;
    Person personEntityL1;

    Long idNotExist = -1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        personController = new PersonController(personService);
        personL1 = new Person(1L, "John Doe", null);
        personEntityL1 = new Person(1L, "John Doe", null);
    }

    public void assertPersonEquals(Person person, Person personAdapter){
        assertEquals(person.getId(), personAdapter.getId());
        assertEquals(person.getName(), personAdapter.getName());
    }

    @Test
    void getAllPersons() {
        when(personService.findAll()).thenReturn(List.of(personEntityL1));
        List<Person> persons = personController.getAllPersons().getBody();
        assertNotNull(persons);
        assertEquals(1, persons.size());
    }

    @Test
    void createPerson() {
        when(personService.save(personL1)).thenReturn(personEntityL1);
        Person personAdapter = personController.createPerson(personL1).getBody();
        assertNotNull(personAdapter);
        assertPersonEquals(personL1, personAdapter);
    }

    @Test
    void getPersonById() {
        when(personService.findById(personL1.getId())).thenReturn(personEntityL1);
        Person personAdapter = personController.getPersonById(personL1.getId()).getBody();
        assertNotNull(personAdapter);
        assertPersonEquals(personL1, personAdapter);
    }

    @Test
    void updatePerson() {
        when(personService.findById(personL1.getId())).thenReturn(personEntityL1);
        when(personService.save(personL1)).thenReturn(personEntityL1);
        Person personAdapter = personController.updatePerson(personL1.getId(), personL1).getBody();
        assertNotNull(personAdapter);
        assertPersonEquals(personL1, personAdapter);
    }

    @Test
    void deletePersonById() {
        when(personService.findById(personL1.getId())).thenReturn(personEntityL1);
        personController.deletePersonById(personL1.getId());
        verify(personService, times(1)).deleteById(personL1.getId());
    }

    @Test
    void getPersonByIdNotFound() {
        when(personService.findById(idNotExist)).thenReturn(null);
        Person personAdapter = personController.getPersonById(idNotExist).getBody();
        assertNull(personAdapter);
    }
}
