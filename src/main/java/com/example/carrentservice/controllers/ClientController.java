package com.example.carrentservice.controllers;

import com.example.carrentservice.entities.Client;
import com.example.carrentservice.services.ClientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/client")
public class ClientController {


    private final ClientService clientService;


    @GetMapping({"id"})
    public ResponseEntity<Client> getClientById(@RequestParam Long id){
        return new ResponseEntity<>(clientService.getClientById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Client> createClientProfile(@Valid @RequestBody Client client){
        client = clientService.createClientProfile(client);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

}
