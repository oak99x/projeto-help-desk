package mfreitas.msuser.DtosTests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import mfreitas.msuser.dtos.UserLoginDTO;

public class UserLoginDtoTests {
    
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    public void testEmptyEmailValidation() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("");

        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("email")));
    }

    @Test
    public void testInvalidEmailFormatValidation() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("invalidemail");

        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("email")));
    }

    @Test
    public void testValidDTO() {
        // Criando um DTO válido com todos os campos preenchidos corretamente
        UserLoginDTO dto = new UserLoginDTO();
        dto.setId(UUID.randomUUID()); // Simulando um UUID válido
        dto.setEmail("validemail@example.com");
        dto.setName("John Doe");
        dto.setPassword("password");
        // Adicionar roles se necessário
        // dto.setRoles(Set.of(...));

        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "Expected no validation errors for valid DTO");
    }
}
