package ru.ryabchuk.testtask.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.ryabchuk.testtask.models.CurrentUser;
import ru.ryabchuk.testtask.models.Person;
import ru.ryabchuk.testtask.repository.PersonRepo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepo personRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CurrentUser currentUser;

    @InjectMocks
    private PersonService personService;

    @Test
    void testSavePerson() {
        Person expectedPerson  = mock(Person.class);
        String originalPassword = "password";

        when(expectedPerson .getPassword()).thenReturn(originalPassword);
        when(passwordEncoder.encode(originalPassword)).thenReturn("encodedPassword");

        personService.savePerson(expectedPerson );

        verify(passwordEncoder).encode(originalPassword);
        verify(personRepo).save(expectedPerson);
        verify(expectedPerson ).setPassword("encodedPassword");
    }

    @Test
    void testFindById() {
        Person expectedPerson  = new Person();
        expectedPerson.setId(1L);

        when(personRepo.findById(1L)).thenReturn(Optional.of(expectedPerson));

        Person actualPerson = personService.findById(1L);

        assertEquals(expectedPerson, actualPerson);
        verify(personRepo).findById(1L);
    }

    @Test
    void testFindEmpty() {
        when(personRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> personService.findById(1L));
        verify(personRepo, times(1)).findById(1L);
    }

    @Test
    void testDeletePerson() {
        Person mockPerson = mock(Person.class);

        when(mockPerson.getId()).thenReturn(1L);
        when(currentUser.getCurrentUser()).thenReturn(mockPerson);

        personService.deletePerson();

        verify(personRepo).deleteById(1L);
    }

    @Test
    void testUpdatePerson() {
        Person mockPerson = mock(Person.class);
        Person personUpdate = mock(Person.class);

        when(currentUser.getCurrentUser()).thenReturn(mockPerson);

        when(personUpdate.getName()).thenReturn("New Name");
        when(personUpdate.getAge()).thenReturn(25);
        when(personUpdate.getPassword()).thenReturn("newPassword");

        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");

        personService.updatePerson(personUpdate);

        verify(personRepo).save(mockPerson);
        verify(mockPerson).setName("New Name");
        verify(mockPerson).setAge(25);
        verify(mockPerson).setPassword("encodedPassword");
    }

    @Test
    void testFindAll() {
        List<Person> personList = Arrays.asList(new Person());
        when(personRepo.findAll()).thenReturn(personList);

        List<Person> result = personService.findAll();

        assertNotNull(result);
        assertEquals(personList.size(), result.size());
        assertEquals(personList.get(0), result.get(0));
        verify(personRepo, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() {
        when(personRepo.findAll()).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> personService.findAll());
        verify(personRepo, times(1)).findAll();
    }
}