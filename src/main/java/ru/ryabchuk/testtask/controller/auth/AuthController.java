package ru.ryabchuk.testtask.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ryabchuk.testtask.models.Person;
import ru.ryabchuk.testtask.models.Token;
import ru.ryabchuk.testtask.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Person personDTO) {
        return ResponseEntity.ok(service.register(personDTO));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Token> auth(@RequestBody AuthenticationRequest personDTO) {
        return ResponseEntity.ok(service.auth(personDTO));
    }
}
