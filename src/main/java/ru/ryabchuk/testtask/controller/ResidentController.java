package ru.ryabchuk.testtask.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ryabchuk.testtask.models.HouseResident;
import ru.ryabchuk.testtask.service.ResidentService;

import java.util.List;

@RestController
@RequestMapping("/api/residents")
@AllArgsConstructor
public class ResidentController {

    private final ResidentService residentService;

    @GetMapping()
    public ResponseEntity<List<HouseResident>> getResidentsByCurrentUser() {
        List<HouseResident> residents = residentService.getResidentsByOwner();
        return ResponseEntity.status(HttpStatus.OK).body(residents);
    }
    @GetMapping("/{id}")
    public ResponseEntity<List<HouseResident>> getResidentsByHouse(@PathVariable Long id) {
        List<HouseResident> residents = residentService.getResidentsByHouseId(id);
        return ResponseEntity.status(HttpStatus.OK).body(residents);
    }
    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> setResident(@PathVariable Long id) {
        residentService.setResident(id);
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> evictResident(@PathVariable Long id) {
        residentService.evictResident(id);
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    }

}
