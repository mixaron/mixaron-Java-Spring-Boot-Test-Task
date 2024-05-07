package ru.ryabchuk.testtask.models;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.ryabchuk.testtask.models.Person;
import ru.ryabchuk.testtask.repository.PersonRepo;
import ru.ryabchuk.testtask.service.PersonService;

import java.util.Optional;


@Component
@AllArgsConstructor
public class CurrentUser {

    private final PersonRepo personRepo;

    public UserDetails getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }
        return null;
    }

    public String getCurrentUsername() {
        UserDetails userDetails = getCurrentUser();
        return (userDetails != null) ? userDetails.getUsername() : null;
    }

    public Person isPerson() {
        String username = getCurrentUsername();
        Person person = personRepo.findByName(username)
                .orElseThrow(() -> new EntityNotFoundException("Person not found with username: " + username));
        return person;
    }

}