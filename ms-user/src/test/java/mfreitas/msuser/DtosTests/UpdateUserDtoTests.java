package mfreitas.msuser.DtosTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import mfreitas.msuser.dtos.UpdateUserDTO;

public class UpdateUserDtoTests {
    @Test
    public void testNoArgsConstructor() {
        UpdateUserDTO dto = new UpdateUserDTO();
        assertNotNull(dto);
    }

    @Test
    public void testAllArgsConstructor() {
        String email = "test@example.com";
        String name = "Test User";
        String password = "testPassword";

        UpdateUserDTO dto = new UpdateUserDTO(email, name, password);

        assertEquals(email, dto.getEmail());
        assertEquals(name, dto.getName());
        assertEquals(password, dto.getPassword());
    }

    @Test
    public void testGettersAndSetters() {
        UpdateUserDTO dto = new UpdateUserDTO();

        dto.setEmail("test@example.com");
        dto.setName("Test User");
        dto.setPassword("testPassword");

        assertEquals("test@example.com", dto.getEmail());
        assertEquals("Test User", dto.getName());
        assertEquals("testPassword", dto.getPassword());
    }
}
