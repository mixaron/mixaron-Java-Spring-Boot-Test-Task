package ru.ryabchuk.testtask.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.ryabchuk.testtask.models.House;
import ru.ryabchuk.testtask.models.HouseResident;
import ru.ryabchuk.testtask.models.Person;
import ru.ryabchuk.testtask.repository.ResidentRepo;
import ru.ryabchuk.testtask.models.CurrentUser;

import java.util.List;

@Service
@AllArgsConstructor
public class ResidentService {
    private final HouseService houseService;

    private final PersonService personService;

    private final CurrentUser currentUser;

    private final ResidentRepo residentRepo;

    public House getInfo() {
        Person person = (Person) currentUser.getCurrentUser();
        House house = houseService.findById(person.getHouse().getId());
        if (house.getOwner().getId() == person.getId()) {
            return house;
        }
        throw new AccessDeniedException("Person with id " + person.getId() + " not owner");
    }

    @Transactional
    public void setResident(Long idResident) {
        House house = getInfo();
        Person person = personService.findById(idResident);
        if (residentRepo.findByResidentId(person.getId()) != null) throw new EntityNotFoundException("Resident already exists");
        HouseResident houseResident = new HouseResident(person, house);
            residentRepo.save(houseResident);
    }
    @Transactional
    public void evictResident(Long idResident) {
        Person person = personService.findById(idResident);
        getInfo();
        residentRepo.deleteByResident(person);
    }


    public List<HouseResident> getResidentsByHouseId(Long houseId) {
        return houseService.getResidentsByHouseId(houseId);
    }
    public List<HouseResident> getResidentsByOwner() {
        if (getInfo().getResidents().isEmpty()) {
            throw new EntityNotFoundException("No one resident found");
        }
        return getInfo().getResidents();
    }
}
