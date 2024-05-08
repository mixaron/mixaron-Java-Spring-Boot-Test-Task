package ru.ryabchuk.testtask.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.ryabchuk.testtask.models.Person;
import ru.ryabchuk.testtask.service.JwtService;
import ru.ryabchuk.testtask.service.PersonService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
@AutoConfigureMockMvc(addFilters = false)
class PersonControllerTest {
    @Autowired
    private WebApplicationContext context;

    @MockBean
    private PersonService personService;

    @MockBean
    private JwtService jwtService;

    private MockMvc mockMvc;



    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testGetAllPersons() throws Exception {
        List<Person> persons = Arrays.asList(
                new Person(1L, "John Doe"),
                new Person(2L, "Jane Doe")
        );
        when(personService.findAll()).thenReturn(persons);

        mockMvc.perform(get("/api/person")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Jane Doe")));
    }
    @Test
    void testGetAllPersons404() throws Exception {

        when(personService.findAll()).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/person")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserById() throws Exception {
        Long id = 1L;
        Person expectedPerson = new Person(id, "John Doe");
        when(personService.findById(id)).thenReturn(expectedPerson);

        mockMvc.perform(get("/api/person/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")));

    }

    @Test
    void testGetPerson404() throws Exception {

        when(personService.findById(1L)).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/person/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    void testDeletePerson() throws Exception {
        doNothing().when(personService).deletePerson();

        mockMvc.perform(delete("/api/person")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}