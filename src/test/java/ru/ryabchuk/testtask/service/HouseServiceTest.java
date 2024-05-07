package ru.ryabchuk.testtask.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.transaction.TestTransaction;
import ru.ryabchuk.testtask.models.CurrentUser;
import ru.ryabchuk.testtask.models.House;
import ru.ryabchuk.testtask.models.HouseResident;
import ru.ryabchuk.testtask.models.Person;
import ru.ryabchuk.testtask.repository.HouseRepo;
import ru.ryabchuk.testtask.repository.ResidentRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HouseServiceTest {
    @Mock
    private  HouseRepo houseRepo;
    @Mock
    private  PersonService personService;
    @Mock
    private  CurrentUser currentUser;
    @Mock
    private  ResidentRepo residentRepo;
    @InjectMocks
    private HouseService houseService;

    @Test
    void testSaveHouse() {
        Person mockPerson = mock(Person.class);
        House mockHouse = mock(House.class);

        when(currentUser.getCurrentUser()).thenReturn(mockPerson);

        houseService.saveHouse(mockHouse);

        verify(mockHouse).setOwner(mockPerson);
        verify(houseRepo).save(mockHouse);
        verify(mockPerson).setHouse(mockHouse);
        verify(personService).savePerson(mockPerson);
        verify(residentRepo).save(any(HouseResident.class));
    }

    @Test
    void testSetNewOwner() {
        Long houseId = 1L;
        Person mockPerson = mock(Person.class);
        House mockHouse = mock(House.class);
        when(currentUser.getCurrentUser()).thenReturn(mockPerson);
        when(houseRepo.findById(houseId)).thenReturn(Optional.of(mockHouse));
        when(mockHouse.getOwner()).thenReturn(null);

        houseService.setNewOwner(houseId);

        verify(mockHouse).setOwner(mockPerson);
        verify(houseRepo).save(mockHouse);
        verify(mockPerson).setHouse(mockHouse);
        verify(personService).savePerson(mockPerson);
        verify(residentRepo).save(any(HouseResident.class));
    }

    @Test
    void testSetNewOwnerWithoutHouse() {
        Long houseId = 1L;
        Person mockPerson = mock(Person.class);
        House mockHouse = mock(House.class);
        when(currentUser.getCurrentUser()).thenReturn(mockPerson);
        when(houseRepo.findById(houseId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> houseService.setNewOwner(houseId));
        verify(mockHouse, never() ).setOwner(any());
        verify(houseRepo, never()).save(any());
    }

    @Test
    void testSetNewOwnerWhenOwnerExists() {
        Long houseId = 1L;
        Person mockPerson = mock(Person.class);
        House mockHouse = mock(House.class);
        when(currentUser.getCurrentUser()).thenReturn(mockPerson);
        when(houseRepo.findById(houseId)).thenReturn(Optional.of(mockHouse));
        when(mockHouse.getOwner()).thenReturn(mockPerson);

        assertThrows(EntityNotFoundException.class, () -> houseService.setNewOwner(houseId));

        verify(mockHouse, never() ).setOwner(any());
        verify(houseRepo, never()).save(any());
    }

    @Test
    void testDeleteHouse() {
        House mockHouse  = new House();
        mockHouse.setId(1L);

        when(houseRepo.findById(1L)).thenReturn(Optional.of(mockHouse));

        houseService.deleteHouse(1L);

        verify(houseRepo).deleteById(1L);
    }

    @Test
    void testDeleteHouseWithoutHouse() {

        when(houseRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> houseService.deleteHouse(1L));

        verify(houseRepo, never()).deleteById(1L);
    }

    @Test
    void testUpdateHouse() {
        House mockHouse  = mock(House.class);
        mockHouse.setId(1L);

        House mockHouseUpdate  = new House();
        mockHouseUpdate.setAddress("New Address");

        when(houseRepo.findById(1L)).thenReturn(Optional.of(mockHouse));

        houseService.updateHouse(1L, mockHouseUpdate);
        verify(mockHouse).setAddress(mockHouseUpdate.getAddress());
        verify(houseRepo).save(mockHouse);
    }

    @Test
    void testUpdateHouseWithoutHouse() {
        House mockHouse  = mock(House.class);
        mockHouse.setId(1L);

        House mockHouseUpdate  = new House();
        mockHouseUpdate.setAddress("New Address");

        when(houseRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> houseService.deleteHouse(1L));

        verify(mockHouse, never()).setAddress(mockHouseUpdate.getAddress());
        verify(houseRepo, never()).save(mockHouse);
    }

    @Test
    void testFindById() {
        House mockHouse  = new House();
        mockHouse.setId(1L);

        when(houseRepo.findById(1L)).thenReturn(Optional.of(mockHouse));


        House actualHouse = houseService.findById(1L);

        assertEquals(mockHouse, actualHouse);
        verify(houseRepo).findById(1L);
    }

    @Test
    void testFindByIdWithoutHouse() {

        when(houseRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> houseService.findById(1L));
    }



    @Test
    void getResidentsByHouseId() {
    }

    @Test
    void testFindAll() {
        List<House> houseList = new ArrayList<>();
        houseList.add(new House());
        when(houseRepo.findAll()).thenReturn(houseList);

        List<House> result = houseService.findAll();

        assertNotNull(result);
        assertEquals(houseList.size(), result.size());
        assertEquals(houseList.get(0), result.get(0));
        verify(houseRepo, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() {
        when(houseRepo.findAll()).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> houseService.findAll());
        verify(houseRepo, times(1)).findAll();
    }
}