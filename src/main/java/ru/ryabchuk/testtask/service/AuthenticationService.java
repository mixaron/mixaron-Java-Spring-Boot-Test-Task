package ru.ryabchuk.testtask.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ryabchuk.testtask.dto.PersonDTO;
import ru.ryabchuk.testtask.models.Person;
import ru.ryabchuk.testtask.models.Token;
import ru.ryabchuk.testtask.repository.PersonRepo;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PersonRepo personRepo;

    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepo.save(person);
    }

    public Token auth(PersonDTO request) {
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
}
