package mfreitas.msuser.ServicesTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import mfreitas.msuser.dtos.UserLoginDTO;
import mfreitas.msuser.exceptions.ObjectNotFoundException;
import mfreitas.msuser.models.User;
import mfreitas.msuser.repositories.UserRepository;
import mfreitas.msuser.services.LoginService;

@SpringBootTest
public class LoginServiceTests {
     @Mock
    private UserRepository userRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private LoginService loginService;

    private String existingEmail = "existing@example.com";
    private String nonExistingEmail = "nonexisting@example.com";

    @BeforeEach
    public void setUp() {
        // Configuração do comportamento do mock para o usuário existente
        User existingUser = new User();
        existingUser.setEmail(existingEmail);
        when(userRepository.findByEmailIgnoreCase(existingEmail)).thenReturn(Optional.of(existingUser));

        // Configuração do comportamento do mock para mensagem de erro
        when(messageSource.getMessage(eq("email.not.found"), any(), any()))
                .thenReturn("Email not found:");
    }

    @Test
    public void shouldReturnUserLoginDTO_whenEmailExists() {
        UserLoginDTO userDTO = loginService.getUserByEmail(existingEmail);
        
        assertNotNull(userDTO);
        assertEquals(existingEmail, userDTO.getEmail());
    }

    @Test
    public void shouldReturnObjectNotFoundException_whenNonExistingEmail() {
        when(userRepository.findByEmailIgnoreCase(nonExistingEmail)).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {
            loginService.getUserByEmail(nonExistingEmail);
        });

        assertEquals("Email not found:" + nonExistingEmail, exception.getMessage());
    }
}
