package com.example.carrentservice.repositories;

import com.example.carrentservice.entities.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ClientRepositoryTest {

    public static final String EMAIL = "john.doe@doe.com";
    @Autowired
    ClientRepository underTest;

    @Test
    void itShouldFindClientExistsByCorrectEmail() {
        //Given
        Client client = new Client("John", "Doe", EMAIL, "+00000000000");
        underTest.save(client);

        //when
        boolean byEmail = underTest.existsByEmail(EMAIL);

        //then
        assertTrue(byEmail);

    }


    @Test
    void itShouldNotFindClientExistsByIncorrectEmail() {
        //Given
        Client client = new Client("John", "Doe", "john.doe@doe.com", "+00000000000");
        underTest.save(client);

        //when
        boolean byEmail = underTest.existsByEmail(EMAIL + "d");

        //then
        assertFalse(byEmail);

    }
}