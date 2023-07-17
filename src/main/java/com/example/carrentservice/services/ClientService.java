package com.example.carrentservice.services;

import com.example.carrentservice.entities.Client;
import com.example.carrentservice.exceptions.IncorrectIdentifierException;
import com.example.carrentservice.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new IncorrectIdentifierException(
                        String.format("ERROR: Client with id=%d doesn't exists", id)));
    }


    public Client createClientProfile(Client client) {
        client.setId(null);
        clientRepository.save(client);
        return client;
    }
}
