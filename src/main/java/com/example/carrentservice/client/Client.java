package com.example.carrentservice.client;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String surname;
    @NotNull
    @NotBlank
    private String email;
    @NotNull
    @NotBlank
    private String telephoneNumber;

    public Client(String name, String surname, String email, String telephoneNumber) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
    }
}
