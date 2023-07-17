package com.example.carrentservice.controllers;

import com.example.carrentservice.entities.Client;
import com.example.carrentservice.exceptions.ErrorResponse;
import com.example.carrentservice.services.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @GetMapping({"{id}"})
    @Operation(summary = "Find client by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Incorrect client ID", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return new ResponseEntity<>(clientService.getClientById(id), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create client profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "422", description = "Email already exists", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public ResponseEntity<Client> createClientProfile(@Valid @RequestBody Client client) {
        client = clientService.createClientProfile(client);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

}
