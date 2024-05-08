package ru.ryabchuk.testtask.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.ryabchuk.testtask.dto.PersonDTO;
import ru.ryabchuk.testtask.models.Person;
import ru.ryabchuk.testtask.models.Token;
import ru.ryabchuk.testtask.repository.PersonRepo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private PersonRepo personRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void register() {
        String rawPassword = "password";
        String encodedPassword = "encodedPassword";
        Person person = new Person();
        person.setPassword(rawPassword);

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        authenticationService.register(person);

        verify(personRepo).save(person);
        assertEquals(encodedPassword, person.getPassword());
    }

    @Test
    void auth() {
        String username = "testUser";
        String password = "testPassword";
        String jwtToken = "testJwtToken";
        PersonDTO request = new PersonDTO(username, password);
        Person person = new Person();
        person.setName(username);
        person.setPassword(password);

        when(personRepo.findByName(username)).thenReturn(Optional.of(person));
        when(jwtService.generateToken(person)).thenReturn(jwtToken);

        Token token = authenticationService.auth(request);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertEquals(jwtToken, token.getToken());
    }

    @Test
    void authPersonNotFound() {
        String username = "testUser";
        PersonDTO request = new PersonDTO(username, "testPassword");

        when(personRepo.findByName(username)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> authenticationService.auth(request));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}