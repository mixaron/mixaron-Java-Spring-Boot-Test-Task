package ru.ryabchuk.testtask.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ryabchuk.testtask.models.House;
import ru.ryabchuk.testtask.models.HouseResident;
import ru.ryabchuk.testtask.models.Person;
import ru.ryabchuk.testtask.repository.HouseRepo;
import ru.ryabchuk.testtask.repository.ResidentRepo;
import ru.ryabchuk.testtask.models.CurrentUser;

import java.util.List;

@Service
@AllArgsConstructor
public class HouseService {

    private final HouseRepo houseRepo;

    private final PersonService personService;

    private final CurrentUser currentUser;

    private final ResidentRepo residentRepo;


    @Transactional
    public void saveHouse(House house) {
        Person person = (Person) currentUser.getCurrentUser();
        if (houseRepo.findByOwnerId(person.getId()) != null) throw new EntityNotFoundException("Owner already exists");
        house.setOwner(person);
        houseRepo.save(house);
        person.setHouse(house);
        personService.savePerson(person);
        residentRepo.save(new HouseResident(person, house));
    }

    @Transactional
    public void setNewOwner(Long idHouse) {
        Person person = (Person) currentUser.getCurrentUser();
        House currentHouse = houseRepo.findById(idHouse)
                .orElseThrow(() -> new EntityNotFoundException("House not found with id: " + idHouse));
        if (currentHouse.getOwner() == null ) {
            currentHouse.setOwner(person);
            houseRepo.save(currentHouse);
            person.setHouse(currentHouse);
            personService.savePerson(person);
            residentRepo.save(new HouseResident(person, currentHouse));
            return;
        }
        throw new EntityNotFoundException("there is already an owner");
    }
    public void deleteHouse(Long id) {
        houseRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("House not found with id: " + id));
        houseRepo.deleteById(id);
    }

    public void updateHouse(Long id, House houseUpdate) {
        House house = houseRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("House not found with id: " + id));
        house.setAddress(houseUpdate.getAddress());
        houseRepo.save(house);
    }
    public House findById(Long id) {
        return houseRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("House not found with id: " + id));
    }

    public List<HouseResident> getResidentsByHouseId(Long id) {
        House house = houseRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("House not found with id: " + id));
        if (house.getResidents().isEmpty()) throw new EntityNotFoundException("Resident in this House not found");
        return house.getResidents();
    }

    public List<House> findAll() {
        List<House> houses = houseRepo.findAll();
        if (houses.isEmpty()) {
            throw new EntityNotFoundException("No houses found");
        }
        return houses;
    }




}
