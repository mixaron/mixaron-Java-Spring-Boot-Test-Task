package ru.ryabchuk.testtask.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ryabchuk.testtask.models.CurrentUser;
import ru.ryabchuk.testtask.models.House;
import ru.ryabchuk.testtask.models.Person;
import ru.ryabchuk.testtask.repository.PersonRepo;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepo personRepo;

    private final PasswordEncoder passwordEncoder;

    private final CurrentUser currentUser;

    @Transactional
    public void savePerson(Person person) {
        String encode = passwordEncoder.encode(person.getPassword());
        person.setPassword(encode);
        personRepo.save(person);
    }

    public Person findById(Long id)  {
        return personRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + id));
    }

    public void deletePerson() {
        Person person = (Person) currentUser.getCurrentUser();
        personRepo.deleteById(person.getId());
    }

    public void updatePerson(Person personUpdate) {
        Person person = (Person) currentUser.getCurrentUser();

        if (personUpdate.getName() != null) {
            person.setName(personUpdate.getName());
        }
        if (personUpdate.getAge() >= 18) {
            person.setAge(personUpdate.getAge());
        }
        if (personUpdate.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(personUpdate.getPassword());
            person.setPassword(encodedPassword);
        }
        personRepo.save(person);
    }

    public List<Person> findAll() {
        List<Person> personList = personRepo.findAll();
        if (personList.isEmpty()) {
            throw new EntityNotFoundException("No one person found");
        }
        return personList;
    }

}
