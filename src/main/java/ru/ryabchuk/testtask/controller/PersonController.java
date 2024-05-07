package ru.ryabchuk.testtask.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ryabchuk.testtask.models.Person;
import ru.ryabchuk.testtask.service.PersonService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/person")
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;


    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getUserById(@PathVariable Long id) {
        Person person = personService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(person);

    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateUser(@Valid @RequestBody Person person) {
        personService.updatePerson(person);
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deletePerson() {
        personService.deletePerson();
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    }

}
