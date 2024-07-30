package mfreitas.msuser.ServicesTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import mfreitas.msuser.dtos.UpdateUserDTO;
import mfreitas.msuser.exceptions.ObjectAlreadyExistsException;
import mfreitas.msuser.exceptions.ObjectNotFoundException;
import mfreitas.msuser.models.User;
import mfreitas.msuser.repositories.RoleRepository;
import mfreitas.msuser.repositories.UserRepository;
import mfreitas.msuser.resources.userMappers.UserMapper;
import mfreitas.msuser.services.UserService;

@SpringBootTest
public class UserServiceExceptionsTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private MessageSource messageSource;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UUID userId;
    private String email;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();
        email = "nonexistent@example.com";

        when(messageSource.getMessage(eq("user.not.found"), any(), any()))
                .thenReturn("User not found: ");
        when(messageSource.getMessage(eq("email.already.exists"), any(), any()))
                .thenReturn("Email already exists: ");
    }

    @Test
    public void shouldReturnObjectNotFoundException_whenUserIDNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {
            userService.getUserById(userId);
        });

        assertEquals("User not found: " + userId, exception.getMessage());
    }

    @Test
    public void shouldReturnObjectNotFoundException_whenUserEmailNotFound() {
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {
            userService.getUserByEmail(email);
        });

        assertEquals("User not found: " + email, exception.getMessage());
    }

    @Test
    public void shouldReturnObjectNotFoundException_whenUserNotFoundInUpdateUser() {
        UpdateUserDTO userDTO = new UpdateUserDTO();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {
            userService.updateUser(userId, userDTO);
        });

        assertEquals("User not found: " + userId, exception.getMessage());
    }

    @Test
    public void shouldReturnObjectAlreadyExistsException_whenUserEmailAlreadyExistsInUpdateUser() {
        UpdateUserDTO userDTO = new UpdateUserDTO();
        userDTO.setEmail(email);

        User user = new User();
        User existingUser = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(existingUser));

        ObjectAlreadyExistsException exception = assertThrows(ObjectAlreadyExistsException.class, () -> {
            userService.updateUser(userId, userDTO);
        });

        assertEquals("Email already exists: " + email, exception.getMessage());
    }
}
