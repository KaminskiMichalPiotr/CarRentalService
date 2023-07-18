package com.example.carrentservice.controllers;

import com.example.carrentservice.entities.Client;
import com.example.carrentservice.exceptions.ErrorResponse;
import com.example.carrentservice.exceptions.IncorrectEntityException;
import com.example.carrentservice.exceptions.IncorrectIdentifierException;
import com.example.carrentservice.repositories.ClientRepository;
import com.example.carrentservice.services.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ClientControllerTest {


    public static final String NUMBER = "+01234567891";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void getClientById_ExistingId_ReturnsOk() throws Exception {
        Client client = new Client("Joe", "Doe", "joe.doe@gmail.com", NUMBER);
        clientRepository.save(client);
        Long clientId = client.getId();

        ResultActions resultActions = mockMvc.perform(get("/api/v1/client/{id}", clientId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(client.getId()))
                .andExpect(jsonPath("$.name").value(client.getName()))
                .andExpect(jsonPath("$.surname").value(client.getSurname()))
                .andExpect(jsonPath("$.email").value(client.getEmail()))
                .andExpect(jsonPath("$.telephoneNumber").value(client.getTelephoneNumber()));

    }

    @Test
    void getClientById_NonExistingId_ReturnsNotFound() throws Exception {
        Long clientId = Long.MAX_VALUE;

        mockMvc.perform(get("/api/v1/client/{id}", clientId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Identifier Error"));
    }

    @Test
    void createClientProfile_ValidClient_ReturnsOk() throws Exception {
        Client client = new Client("Joe", "Doe", "joe.doe@gmail.com", NUMBER);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.name").value(client.getName()))
                .andExpect(jsonPath("$.surname").value(client.getSurname()))
                .andExpect(jsonPath("$.email").value(client.getEmail()))
                .andExpect(jsonPath("$.telephoneNumber").value(client.getTelephoneNumber()));
    }

    @Test
    void createClientProfile_InvalidClient_ReturnsBadRequest() throws Exception {
        // Invalid client with missing required fields
        Client client = new Client("Joe", "Doe", "joe.doe@gmail.com", NUMBER);
        client.setName(null);
        client.setEmail(null);

        mockMvc.perform(post("/api/v1/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createClientProfile_DuplicateEmail_ReturnsUnprocessableEntity() throws Exception {
        Client client = new Client("Joe", "Doe", "joe.doe@gmail.com", NUMBER);
        clientRepository.save(client);
        Client client2 = new Client("Jon", "Dov", "joe.doe@gmail.com", NUMBER);

        mockMvc.perform(post("/api/v1/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client2)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Entity isn't valid"));
    }

}