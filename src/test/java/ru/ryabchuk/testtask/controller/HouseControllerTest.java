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
import ru.ryabchuk.testtask.models.House;
import ru.ryabchuk.testtask.service.HouseService;
import ru.ryabchuk.testtask.service.JwtService;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HouseController.class)
@AutoConfigureMockMvc(addFilters = false)
class HouseControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private HouseService houseService;

    @MockBean
    private JwtService jwtService;

    private MockMvc mockMvc;



    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testGetAllHouses() throws Exception {
        House house1 = new House(1L, "Address 1");
        House house2 = new House(2L, "Address 2");
        when(houseService.findAll()).thenReturn(Arrays.asList(house1, house2));

        mockMvc.perform(get("/api/house")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].address", is("Address 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].address", is("Address 2")));
    }

    @Test
    void testGetHouseById() throws Exception {
        Long houseId = 1L;
        House house = new House(houseId, "Address 1");
        when(houseService.findById(houseId)).thenReturn(house);

        mockMvc.perform(get("/api/house/{id}", houseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.address", is("Address 1")));
    }

    @Test
    void testCreateHouse() throws Exception {
        House house = new House(1L, "Address 1");
        doNothing().when(houseService).saveHouse(house);

        String houseJson = "{\"id\":1,\"address\":\"Address 1\"}";

        mockMvc.perform(post("/api/house")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(houseJson))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateHouse() throws Exception {
        Long houseId = 1L;
        House house = new House(houseId, "Updated Address");
        doNothing().when(houseService).updateHouse(houseId, house);
        String houseJson = "{\"id\":1,\"address\":\"Updated Address\"}";
        mockMvc.perform(put("/api/house/{id}", houseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(houseJson))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteHouse() throws Exception {
        Long houseId = 1L;
        doNothing().when(houseService).deleteHouse(houseId);

        mockMvc.perform(delete("/api/house/{id}", houseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testSetNewOwner() throws Exception {
        Long houseId = 1L;
        doNothing().when(houseService).setNewOwner(houseId);

        mockMvc.perform(post("/api/house/{id}", houseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetHouseByIdNotFound() throws Exception {
        Long houseId = 1L;
        doThrow(new EntityNotFoundException()).when(houseService).findById(houseId);

        mockMvc.perform(get("/api/house/{id}", houseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateHouseNotFound() throws Exception {
        Long houseId = 1L;
        House house = new House(houseId, "Updated Address");
        doThrow(new EntityNotFoundException()).when(houseService).updateHouse(houseId, house);

        mockMvc.perform(put("/api/house/{id}", houseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteHouseNotFound() throws Exception {
        Long houseId = 1L;
        doThrow(new EntityNotFoundException()).when(houseService).deleteHouse(houseId);

        mockMvc.perform(delete("/api/house/{id}", houseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}