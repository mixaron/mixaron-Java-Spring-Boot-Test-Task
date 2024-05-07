package ru.ryabchuk.testtask.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import ru.ryabchuk.testtask.models.CurrentUser;
import ru.ryabchuk.testtask.models.House;
import ru.ryabchuk.testtask.models.HouseResident;
import ru.ryabchuk.testtask.models.Person;
import ru.ryabchuk.testtask.repository.ResidentRepo;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResidentServiceTest {
    @Mock
    private HouseService houseService;
    @Mock
    private PersonService personService;
    @Mock
    private CurrentUser currentUser;
    @Mock
    private ResidentRepo residentRepo;
    @InjectMocks
    private ResidentService residentService;

    @Test
    void testGetInfo() {
        Person person = mock(Person.class);
        House house = mock(House.class);
        house.setId(1L);

        when(currentUser.getCurrentUser()).thenReturn(person);
        when(person.getHouse()).thenReturn(house);
        when(person.getId()).thenReturn(1L);
        when(house.getOwner()).thenReturn(person);
        when(house.getId()).thenReturn(1L);
        when(houseService.findById(1L)).thenReturn(house);
        House result = residentService.getInfo();

        assertEquals(house, result);
        verify(houseService).findById(1L);
    }

    @Test
    void testGetInfoWhenPersonIsNotOwner() {
        Person person = mock(Person.class);
        House house = mock(House.class);
        Person owner = mock(Person.class);

        when(person.getHouse()).thenReturn(house);
        when(person.getId()).thenReturn(1L);
        when(house.getOwner()).thenReturn(owner);
        when(house.getId()).thenReturn(1L);
        when(currentUser.getCurrentUser()).thenReturn(person);
        when(houseService.findById(1L)).thenReturn(house);

        assertThrows(AccessDeniedException.class, () -> residentService.getInfo());
        verify(houseService).findById(1L);
    }

    @Test
    void testSetResident() {
        Person person = mock(Person.class);
        House house = mock(House.class);
        house.setId(1L);

        when(currentUser.getCurrentUser()).thenReturn(person);
        when(person.getHouse()).thenReturn(house);
        when(person.getId()).thenReturn(1L);
        when(house.getOwner()).thenReturn(person);
        when(house.getId()).thenReturn(1L);
        when(houseService.findById(1L)).thenReturn(house);

        when(personService.findById(1L)).thenReturn(person);

        residentService.setResident(1L);

        verify(residentRepo).save(any(HouseResident.class));
    }

    @Test
    void testEvictResident() {
        Person person = mock(Person.class);
        House house = mock(House.class);
        house.setId(1L);

        when(currentUser.getCurrentUser()).thenReturn(person);
        when(person.getHouse()).thenReturn(house);
        when(person.getId()).thenReturn(1L);
        when(house.getOwner()).thenReturn(person);
        when(house.getId()).thenReturn(1L);
        when(houseService.findById(1L)).thenReturn(house);

        when(personService.findById(1L)).thenReturn(person);

        residentService.evictResident(1L);

        verify(residentRepo).deleteByResident(person);
    }
    @Test
    void getResidentsByHouseId() {
        HouseResident resident1 = new HouseResident();
        HouseResident resident2 = new HouseResident();
        List<HouseResident> residents = Arrays.asList(resident1, resident2);

        when(houseService.getResidentsByHouseId(1L)).thenReturn(residents);

        List<HouseResident> result = residentService.getResidentsByHouseId(1L);

        assertEquals(residents, result);
    }
}