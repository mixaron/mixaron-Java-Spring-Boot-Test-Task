package ru.ryabchuk.testtask.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ryabchuk.testtask.controller.auth.AuthenticationRequest;
import ru.ryabchuk.testtask.models.Person;
import ru.ryabchuk.testtask.models.Token;
import ru.ryabchuk.testtask.repository.PersonRepo;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PersonRepo personRepo;

    private final PasswordEncoder passwordEncoder;

//    private final ModelMapper model;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public String register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepo.save(person);
        return "ok";
    }

    public Token auth(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getName(),
                        request.getPassword()
                )
        );
        var user = personRepo.findByName(request.getName())
                .orElseThrow(() -> new EntityNotFoundException("Person not found with username: " + request.getName()));
        var jwtToken = jwtService.generateToken(user);
        return Token.builder().token(jwtToken).build();
    }
//    private Person convertToPerson(PersonDTO personDTO) {
//        return model.map(personDTO, Person.class);
//    }
}
