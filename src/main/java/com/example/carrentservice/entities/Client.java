package com.example.carrentservice.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @NotNull(message = "ERROR: Name cannot be null")
    @NotBlank(message = "ERROR: Name cannot be blank")
    private String name;

    @NotNull(message = "ERROR: Surname cannot be null")
    @NotBlank(message = "ERROR: Surname cannot be blank")
    private String surname;

    @NotNull(message = "ERROR: Email cannot be null")
    @NotBlank(message = "ERROR: Email cannot be blank")
    @Email(message = "ERROR: Invalid email address")
    @Column(unique = true)
    private String email;

    @NotNull(message = "ERROR: Telephone number cannot be null")
    @NotBlank(message = "ERROR: Telephone number cannot be blank")
    @Pattern(regexp = "\\+\\d{11}", message = "ERROR: Invalid telephone number format. It should start with " +
            "'+' followed by 11 digits.")
    private String telephoneNumber;

    public Client(String name, String surname, String email, String telephoneNumber) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
    }
}
