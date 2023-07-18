package com.example.carrentservice.services;

import com.example.carrentservice.entities.Client;
import com.example.carrentservice.exceptions.IncorrectEntityException;
import com.example.carrentservice.exceptions.IncorrectIdentifierException;
import com.example.carrentservice.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getClientById_ExistingId_ReturnsClient() {
        // Prepare test data
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);

        // Mock behavior
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // Perform the test
        Client result = clientService.getClientById(clientId);

        // Assert the result
        assertNotNull(result);
        assertEquals(clientId, result.getId());
    }

    @Test
    void getClientById_NonExistingId_ThrowsIncorrectIdentifierException() {
        // Prepare test data
        Long clientId = 1L;

        // Mock behavior
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Perform the test and assert the exception
        assertThrows(IncorrectIdentifierException.class, () -> clientService.getClientById(clientId));
    }

    @Test
    void createClientProfile_NewClient_SuccessfullyCreatesClient() {
        // Prepare test data
        Client client = new Client();
        client.setEmail("test@example.com");

        // Mock behavior
        when(clientRepository.existsByEmail(client.getEmail())).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // Perform the test
        Client result = clientService.createClientProfile(client);

        // Assert the result
        assertNotNull(result);
        assertEquals(client.getEmail(), result.getEmail());
    }

    @Test
    void createClientProfile_ClientWithEmailExists_ThrowsIncorrectEntityException() {
        // Prepare test data
        Client client = new Client();
        client.setId(null);
        client.setEmail("test@example.com");

        // Mock behavior
        when(clientRepository.existsByEmail(client.getEmail())).thenReturn(true);

        // Perform the test and assert the exception
        assertThrows(IncorrectEntityException.class, () -> clientService.createClientProfile(client));
    }

}