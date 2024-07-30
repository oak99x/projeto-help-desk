package mfreitas.msuser.ServicesTests;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import mfreitas.msuser.dtos.UpdateUserDTO;
import mfreitas.msuser.dtos.UserDTO;
import mfreitas.msuser.models.User;
import mfreitas.msuser.repositories.RoleRepository;
import mfreitas.msuser.repositories.UserRepository;
import mfreitas.msuser.resources.userMappers.UserMapper;
import mfreitas.msuser.services.UserService;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class UserServiceTests {

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void  shouldReturnUserDTO_whenUserIdExists(){
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@gmail.com");

        // Setup
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void shouldReturnUserDTO_whenEmailExists(){
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@gmail.com");

        // Setup
        when(userRepository.findByEmailIgnoreCase("test@gmail.com")).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserByEmail("test@gmail.com");

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void shouldUpdateUserSuccessfully() {

        UUID userId = UUID.randomUUID();

        //new data
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setEmail("newemail@example.com");
        updateUserDTO.setName("New Name");
        updateUserDTO.setPassword("newpassword");

        //old data
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("oldemail@example.com");
        existingUser.setName("Old Name");
        existingUser.setPassword("oldpassword");

        // Setup
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmailIgnoreCase(updateUserDTO.getEmail())).thenReturn(null);
        
        when(passwordEncoder.encode(updateUserDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //Action
        UserDTO result = userService.updateUser(userId, updateUserDTO);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(updateUserDTO.getEmail());
        assertThat(result.getName()).isEqualTo(updateUserDTO.getName());
        assertThat(existingUser.getPassword()).isEqualTo("encodedPassword");
    }
}
