package ru.ryabchuk.testtask.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.ryabchuk.testtask.models.HouseResident;
import ru.ryabchuk.testtask.service.JwtService;
import ru.ryabchuk.testtask.service.ResidentService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResidentController.class)
@AutoConfigureMockMvc(addFilters = false)
class ResidentControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private ResidentService residentService;

    @MockBean
    private JwtService jwtService;

    private MockMvc mockMvc;



    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testGetResidentsByCurrentUser() throws Exception {
        List<HouseResident> residents = Arrays.asList(new HouseResident(), new HouseResident());
        when(residentService.getResidentsByOwner()).thenReturn(residents);

        mockMvc.perform(get("/api/residents")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetResidentsByHouse() throws Exception {
        Long houseId = 1L;
        List<HouseResident> residents = Arrays.asList(new HouseResident(), new HouseResident());
        when(residentService.getResidentsByHouseId(houseId)).thenReturn(residents);

        mockMvc.perform(get("/api/residents/{id}", houseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testSetResident() throws Exception {
        Long houseId = 1L;
        doNothing().when(residentService).setResident(houseId);

        mockMvc.perform(post("/api/residents/{id}", houseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testEvictResident() throws Exception {
        Long houseId = 1L;
        doNothing().when(residentService).evictResident(houseId);

        mockMvc.perform(delete("/api/residents/{id}", houseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetResidentsByCurrentUserNoResidents() throws Exception {
        when(residentService.getResidentsByOwner()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/residents")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetResidentsByHouseNoResidents() throws Exception {
        // Arrange
        Long houseId = 1L;
        when(residentService.getResidentsByHouseId(houseId)).thenReturn(Collections.emptyList());

        // Act and Assert
        mockMvc.perform(get("/api/residents/{id}", houseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testSetResidentNotFound() throws Exception {
        // Arrange
        Long houseId = 1L;
        doThrow(new EntityNotFoundException()).when(residentService).setResident(houseId);

        // Act and Assert
        mockMvc.perform(post("/api/residents/{id}", houseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEvictResidentNotFound() throws Exception {
        Long houseId = 1L;
        doThrow(new EntityNotFoundException("Resident not found")).when(residentService).evictResident(houseId);

            mockMvc.perform(delete("/api/residents/{id}", houseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}