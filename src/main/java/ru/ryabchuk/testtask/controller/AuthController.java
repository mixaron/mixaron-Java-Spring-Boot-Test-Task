package ru.ryabchuk.testtask.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ryabchuk.testtask.dto.PersonDTO;
import ru.ryabchuk.testtask.models.Person;
import ru.ryabchuk.testtask.models.Token;
import ru.ryabchuk.testtask.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@Valid  @RequestBody  Person person) {
        service.register(person);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Token> auth(@RequestBody PersonDTO personDTO) {
        return ResponseEntity.ok(service.auth(personDTO));
    }
}
