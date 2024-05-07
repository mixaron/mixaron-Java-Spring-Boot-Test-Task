package ru.ryabchuk.testtask.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ryabchuk.testtask.models.House;
import ru.ryabchuk.testtask.models.Person;
import ru.ryabchuk.testtask.service.HouseService;

import java.util.List;

@RestController
@RequestMapping("/api/house")
@AllArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @GetMapping
    public ResponseEntity<List<House>> getAllHouses() {
        return ResponseEntity.ok(houseService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<House> getHouseById(@PathVariable Long id) {
        House house = houseService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(house);

    }

    @PostMapping
    public ResponseEntity<HttpStatus> createHouse(@Valid @RequestBody  House house) {
        houseService.saveHouse( house);
        return ResponseEntity.status(HttpStatus.CREATED).body(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateHouse(@PathVariable Long id, @Valid @RequestBody House house) {
        houseService.updateHouse(id, house);
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePerson(@PathVariable Long id) {
        houseService.deleteHouse(id);
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> setNewOrder(@PathVariable Long id) {
        houseService.setNewOwner(id);
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    }
}
