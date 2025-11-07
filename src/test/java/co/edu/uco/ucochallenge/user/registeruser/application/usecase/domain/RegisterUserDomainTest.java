package co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para RegisterUserDomain")
class RegisterUserDomainTest {

    @Test
    @DisplayName("Debería crear dominio con constructor vacío")
    void shouldCreateDomainWithEmptyConstructor() {
        // Act
        RegisterUserDomain domain = new RegisterUserDomain();

        // Assert
        assertNotNull(domain);
        assertNull(domain.getIdType());
        assertNull(domain.getIdNumber());
    }

    @Test
    @DisplayName("Debería crear dominio con constructor completo")
    void shouldCreateDomainWithFullConstructor() {
        // Arrange
        UUID idType = UUID.randomUUID();
        UUID homeCity = UUID.randomUUID();

        // Act
        RegisterUserDomain domain = new RegisterUserDomain(
            idType,
            "123456",
            "Juan",
            "Carlos",
            "Pérez",
            "García",
            homeCity,
            "juan@example.com",
            "3001234567"
        );

        // Assert
        assertEquals(idType, domain.getIdType());
        assertEquals("123456", domain.getIdNumber());
        assertEquals("Juan", domain.getFirstName());
        assertEquals("Carlos", domain.getSecondName());
        assertEquals("Pérez", domain.getFirstSurname());
        assertEquals("García", domain.getSecondSurname());
        assertEquals(homeCity, domain.getHomeCity());
        assertEquals("juan@example.com", domain.getEmail());
        assertEquals("3001234567", domain.getMobileNumber());
    }

    @Test
    @DisplayName("Debería permitir setters")
    void shouldAllowSetters() {
        // Arrange
        RegisterUserDomain domain = new RegisterUserDomain();
        UUID idType = UUID.randomUUID();

        // Act
        domain.setIdType(idType);
        domain.setIdNumber("123456");
        domain.setFirstName("Juan");
        domain.setEmail("juan@example.com");

        // Assert
        assertEquals(idType, domain.getIdType());
        assertEquals("123456", domain.getIdNumber());
        assertEquals("Juan", domain.getFirstName());
        assertEquals("juan@example.com", domain.getEmail());
    }
}
