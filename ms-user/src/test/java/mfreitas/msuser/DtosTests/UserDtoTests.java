package mfreitas.msuser.DtosTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import mfreitas.msuser.dtos.UserDTO;
import mfreitas.msuser.models.Role;

public class UserDtoTests {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    public void testEmptyConstructor() {
        UserDTO dto = new UserDTO();
        assertNotNull(dto);
    }

    @Test
    public void testGettersAndSetters() {
        UserDTO dto = new UserDTO();

        UUID id = UUID.randomUUID();
        String email = "test@example.com";
        String name = "Test User";
        Set<Role> roles = new HashSet<>();

        dto.setId(id);
        dto.setEmail(email);
        dto.setName(name);
        dto.setRoles(roles);

        assertEquals(id, dto.getId());
        assertEquals(email, dto.getEmail());
        assertEquals(name, dto.getName());
        assertEquals(roles, dto.getRoles());
    }

    @Test
    public void testEmailValidation_ValidEmail() {
        UserDTO dto = new UserDTO();
        dto.setEmail("test@example.com");

        assertEquals(0, validator.validateProperty(dto, "email").size());
    }

    @Test
    public void testEmailValidation_InvalidEmail() {
        UserDTO dto = new UserDTO();
        dto.setEmail("invalid-email");

        assertEquals(1, validator.validateProperty(dto, "email").size());
    }

    @Test
    public void testEmailValidation_EmptyEmail() {
        UserDTO dto = new UserDTO();
        dto.setEmail("");

        assertEquals(1, validator.validateProperty(dto, "email").size());
    }

    @Test
    public void testNameValidation_ValidName() {
        UserDTO dto = new UserDTO();
        dto.setName("Test User");

        assertEquals(0, validator.validateProperty(dto, "name").size());
    }

    @Test
    public void testNameValidation_EmptyName() {
        UserDTO dto = new UserDTO();
        dto.setName("");

        assertEquals(0, validator.validateProperty(dto, "name").size());
    }

    @Test
    public void testRolesValidation_NullRoles() {
        UserDTO dto = new UserDTO();
        dto.setRoles(null);

        assertEquals(0, validator.validateProperty(dto, "roles").size());
    }

    @Test
    public void testRolesValidation_EmptyRoles() {
        UserDTO dto = new UserDTO();
        dto.setRoles(new HashSet<>());

        assertEquals(0, validator.validateProperty(dto, "roles").size());
    }
}
