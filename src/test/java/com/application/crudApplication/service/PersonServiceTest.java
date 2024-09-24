package com.application.crudApplication.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import com.application.crudApplication.entity.Employee;
import com.application.crudApplication.entity.Person;
import com.application.crudApplication.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

public class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private PersonService personService;

    private Person testPerson;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testPerson = new Employee(); // or use a concrete subclass of Person
        testPerson.setId(1L);
        testPerson.setName("John Doe");
    }

    // Test case for successful person creation
    @Test
    public void testCreatePersonSuccess() {
        when(personRepository.save(testPerson)).thenReturn(testPerson);

        Person createdPerson = personService.createPerson(testPerson);

        assertNotNull(createdPerson);
        assertEquals(1L, createdPerson.getId());
        verify(personRepository, times(1)).save(testPerson);
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }

    // Test case for getAllPersons() returning list successfully
    @Test
    public void testGetAllPersonsSuccess() {
        List<Person> personList = new ArrayList<>();
        personList.add(testPerson);

        when(personRepository.findAll()).thenReturn(personList);

        List<Person> result = personService.getAllPersons();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(personRepository, times(1)).findAll();
    }

    // Test case for updatePerson() success scenario
    @Test
    public void testUpdatePersonSuccess() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(testPerson));

        Person updatedDetails = new Employee();
        updatedDetails.setName("Jane Doe");
        when(personRepository.save(any(Person.class))).thenReturn(updatedDetails);
        Person updatedPerson = personService.updatePerson(1L, updatedDetails);

        assertEquals("Jane Doe", updatedPerson.getName());
        verify(personRepository, times(1)).save(testPerson);
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }

    // Test case for deletePerson() success scenario
    @Test
    public void testDeletePersonSuccess() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(testPerson));

        personService.deletePerson(1L);

        verify(personRepository, times(1)).deleteById(1L);
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }

    // Failure scenario for updatePerson() when ID is not found
    @Test
    public void testUpdatePersonFailurePersonNotFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        Person updatedDetails = new Employee();
        updatedDetails.setName("Jane Doe");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            personService.updatePerson(1L, updatedDetails);
        });

        String expectedMessage = "Person not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(personRepository, times(0)).save(any(Person.class));
        verify(kafkaTemplate, times(0)).send(anyString(), anyString());
    }

    // Failure scenario for deletePerson() when ID is not found
    @Test
    public void testDeletePersonFailurePersonNotFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            personService.deletePerson(1L);
        });

        String expectedMessage = "Person not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(personRepository, times(0)).deleteById(anyLong());
        verify(kafkaTemplate, times(0)).send(anyString(), anyString());
    }
}
