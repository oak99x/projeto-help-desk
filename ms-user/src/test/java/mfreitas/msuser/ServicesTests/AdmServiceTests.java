package mfreitas.msuser.ServicesTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import mfreitas.msuser.dtos.UserDTO;
import mfreitas.msuser.dtos.UserFilter;
import mfreitas.msuser.exceptions.ObjectAlreadyExistsException;
import mfreitas.msuser.exceptions.ObjectNotFoundException;
import mfreitas.msuser.models.Role;
import mfreitas.msuser.models.User;
import mfreitas.msuser.repositories.RoleRepository;
import mfreitas.msuser.repositories.UserRepository;
import mfreitas.msuser.resources.enums.RoleEnum;
import mfreitas.msuser.services.AdmService;
import mfreitas.msuser.services.SearchUsers;

@SpringBootTest
public class AdmServiceTests {

    @Mock
    private MessageSource messageSource;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private SearchUsers searchUsers;

    @InjectMocks
    private AdmService admService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        reset(messageSource, passwordEncoder, userRepository, roleRepository);
    }


    @Test
    public void testGetAllUsers() {
        User user1 = new User(UUID.randomUUID(), "John Doe", "john.doe@example.com", "password", new HashSet<>());
        User user2 = new User(UUID.randomUUID(), "Jane Doe", "jane.doe@example.com", "password", new HashSet<>());
        List<User> userList = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<UserDTO> userDTOList = admService.getAllUsers();

        assertEquals(2, userDTOList.size());
        assertEquals("John Doe", userDTOList.get(0).getName());
        assertEquals("Jane Doe", userDTOList.get(1).getName());
    }

    @Test
    public void testGetUsersByName() {
        String name = "John";
        User user = new User(UUID.randomUUID(), "John Doe", "john.doe@example.com", "password", new HashSet<>());
        List<User> userList = Arrays.asList(user);

        when(userRepository.findByNameContainingIgnoreCase(name)).thenReturn(userList);

        List<UserDTO> userDTOList = admService.getUsersByName(name);

        assertEquals(1, userDTOList.size());
        assertEquals("John Doe", userDTOList.get(0).getName());
    }

    @Test
    public void testGetUsersByEmail() {
        String email = "john.doe@example.com";
        User user = new User(UUID.randomUUID(), "John Doe", email, "password", new HashSet<>());
        List<User> userList = Arrays.asList(user);

        when(userRepository.findByEmailContainingIgnoreCase(email)).thenReturn(userList);

        List<UserDTO> userDTOList = admService.getUsersByEmail(email);

        assertEquals(1, userDTOList.size());
        assertEquals(email, userDTOList.get(0).getEmail());
    }

    @Test
    public void testGetUsersByRole() {
        String roleName = "ROLE_USER";
        Role role = new Role(UUID.randomUUID(), roleName);

        User user = new User(UUID.randomUUID(), "John Doe", "john.doe@example.com", "password", new HashSet<>(Arrays.asList(role)));
        
        List<User> userList = Arrays.asList(user);
        when(userRepository.findByRoles_RoleNameIgnoreCase(roleName)).thenReturn(userList);

        List<UserDTO> userDTOList = admService.getUsersByRole(roleName);

        assertEquals(1, userDTOList.size());
        assertEquals("John Doe", userDTOList.get(0).getName());
        assertEquals("john.doe@example.com", userDTOList.get(0).getEmail());
    }

    @Test
    @DirtiesContext
    public void testAddUser_Success() {
        UserDTO userDto = new UserDTO();
        userDto.setName("John Doe");
        userDto.setEmail("john.doe@example.com");

        when(userRepository.findByEmailIgnoreCase(userDto.getEmail())).thenReturn(Optional.empty());

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode("john.doe")).thenReturn(encodedPassword);

        Role userRole = new Role();
        userRole.setRoleName("ROLE_USER");
        when(roleRepository.findByRoleNameIgnoreCase(RoleEnum.ROLE_USER.toString())).thenReturn(Optional.of(userRole));

        User newUser = new User();
        newUser.setEmail(userDto.getEmail());
        newUser.setName(userDto.getName());
        newUser.setPassword(encodedPassword);
        newUser.addRole(userRole);

        when(userRepository.save(newUser)).thenReturn(newUser);

        UserDTO savedUserDto = admService.addUser(userDto);

        assertEquals(userDto.getEmail(), savedUserDto.getEmail());
        assertEquals(userDto.getName(), savedUserDto.getName());
        verify(userRepository).save(newUser);
    }

    @Test
    public void testAddUser_UserAlreadyExists() {
        UserDTO userDto = new UserDTO();
        userDto.setEmail("existing.email@example.com");

        when(userRepository.findByEmailIgnoreCase(userDto.getEmail())).thenReturn(Optional.of(new User()));
        when(messageSource.getMessage("email.already.exists", null, null)).thenReturn("Email already exists: ");

        Exception exception = assertThrows(ObjectAlreadyExistsException.class, () -> {
            admService.addUser(userDto);
        });

        assertEquals("Email already exists: existing.email@example.com", exception.getMessage());
    }

    @Test
    public void testAddUser_RoleNotFound() {
        UserDTO userDto = new UserDTO();
        userDto.setEmail("new.email@example.com");

        when(userRepository.findByEmailIgnoreCase(userDto.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findByRoleNameIgnoreCase(RoleEnum.ROLE_USER.toString())).thenReturn(Optional.empty());
        when(messageSource.getMessage("role.not.found", null, null)).thenReturn("Role not found");

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {
            admService.addUser(userDto);
        });

        assertEquals("Role not found", exception.getMessage());
    }

    @Test
    public void testPromoteToAdmin_Success() throws Exception {
        String email = "user@example.com";

        User user = new User();
        user.setEmail(email);

        Role userRole = new Role();
        userRole.setRoleName(RoleEnum.ROLE_USER.toString());
        user.addRole(userRole);

        Role adminRole = new Role();
        adminRole.setRoleName(RoleEnum.ROLE_ADMIN.toString());

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(user));
        when(roleRepository.findByRoleNameIgnoreCase(RoleEnum.ROLE_ADMIN.toString())).thenReturn(Optional.of(adminRole));

        UserDTO result = admService.promoteToAdmin(email);

        assertEquals(email, result.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    public void testPromoteToAdmin_UserNotFound() {
        String email = "non.existent@example.com";

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());
        when(messageSource.getMessage("user.not.found", null, null)).thenReturn("User not found: ");

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            admService.promoteToAdmin(email);
        });

        assertEquals("User not found: non.existent@example.com", exception.getMessage());
    }

    @Test
    public void testPromoteToAdmin_UserAlreadyAdmin() {
        String email = "existing.admin@example.com";

        User adminUser = new User();
        adminUser.setEmail(email);
        Role adminRole = new Role();
        adminRole.setRoleName(RoleEnum.ROLE_ADMIN.toString());
        adminUser.addRole(adminRole);

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(adminUser));
        when(messageSource.getMessage("user.is.already.an.administrator", null, null)).thenReturn("User is already an administrator: ");

        Exception exception = assertThrows(ObjectAlreadyExistsException.class, () -> {
            admService.promoteToAdmin(email);
        });

        assertEquals("User is already an administrator: existing.admin@example.com", exception.getMessage());
        verify(userRepository, never()).save(adminUser);
    }

    @Test
    public void testPromoteToAdmin_RoleNotFound() {
        String email = "user@example.com";

        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(user));
        when(roleRepository.findByRoleNameIgnoreCase(RoleEnum.ROLE_ADMIN.toString())).thenReturn(Optional.empty());
        when(messageSource.getMessage("role.not.found", null, null)).thenReturn("Role not found");

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            admService.promoteToAdmin(email);
        });

        assertEquals("Role not found", exception.getMessage());
        verify(userRepository, never()).save(user);
    }

    @Test
    public void testDeleteRoleAdmin_Success() {
        String email = "admin.user@example.com";

        User user = new User();
        user.setEmail(email);

        Role adminRole = new Role();
        adminRole.setRoleName(RoleEnum.ROLE_ADMIN.toString());
        Role userRole = new Role();
        userRole.setRoleName(RoleEnum.ROLE_USER.toString());

        user.setRoles(Stream.of(adminRole, userRole).collect(Collectors.toSet()));

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(user));

        UserDTO result = admService.deleteRoleAdmin(email);

        assertEquals(email, result.getEmail());
        assertFalse(result.getRoles().contains(RoleEnum.ROLE_ADMIN.toString()));
        verify(userRepository).save(user);
    }

    @Test
    public void testDeleteRoleAdmin_UserNotFound() {
        String email = "non.existent@example.com";

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());
        when(messageSource.getMessage("user.not.found", null, null)).thenReturn("User not found: ");

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            admService.deleteRoleAdmin(email);
        });

        assertEquals("User not found: non.existent@example.com", exception.getMessage());
    }

    @Test
    public void testDeleteRoleAdmin_UserDoesNotHaveAdminRole() {
        String email = "user.without.admin@example.com";

        User user = new User();
        user.setEmail(email);

        Role userRole = new Role();
        userRole.setRoleName(RoleEnum.ROLE_USER.toString());
        user.setRoles(Set.of(userRole));

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(user));
        when(messageSource.getMessage("User does not have the role Admin:", null, null)).thenReturn("User does not have the role Admin: ");

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
            admService.deleteRoleAdmin(email);
        });

        assertEquals("User does not have the role Admin: user.without.admin@example.com", exception.getMessage());
        verify(userRepository, never()).save(user);
    }

    @Test
    public void testFilterUsers_NoFilters() {
        UserFilter userFilter = new UserFilter(); // Filtros vazios
        Specification<User> spec = mock(Specification.class);

        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setEmail("user1@example.com");
        user1.setName("User One");

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setEmail("user2@example.com");
        user2.setName("User Two");

        when(searchUsers.filter(userFilter)).thenReturn(spec);
        when(userRepository.findAll(spec)).thenReturn(List.of(user1, user2));

        List<UserDTO> result = admService.filterUsers(userFilter);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(userDto -> userDto.getEmail().equals("user1@example.com")));
        assertTrue(result.stream().anyMatch(userDto -> userDto.getEmail().equals("user2@example.com")));

        verify(searchUsers).filter(userFilter);
        verify(userRepository).findAll(spec);
    }

    @Test
    public void testFilterUsers_WithFilters() {
        UserFilter userFilter = new UserFilter();
        userFilter.setUserEmail("user1@example.com"); // Filtro por email
        Specification<User> spec = mock(Specification.class);

        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setEmail("user1@example.com");
        user1.setName("User One");

        when(searchUsers.filter(userFilter)).thenReturn(spec);
        when(userRepository.findAll(spec)).thenReturn(List.of(user1));

        List<UserDTO> result = admService.filterUsers(userFilter);

        assertEquals(1, result.size());
        assertEquals("user1@example.com", result.get(0).getEmail());

        verify(searchUsers).filter(userFilter);
        verify(userRepository).findAll(spec);
    }

    @Test
    public void testFilterUsers_NoResults() {
        UserFilter userFilter = new UserFilter();
        userFilter.setUserEmail("user1@example.com");
        Specification<User> spec = mock(Specification.class);

        when(searchUsers.filter(userFilter)).thenReturn(spec);
        when(userRepository.findAll(spec)).thenReturn(List.of());

        List<UserDTO> result = admService.filterUsers(userFilter);

        assertTrue(result.isEmpty());

        verify(searchUsers).filter(userFilter);
        verify(userRepository).findAll(spec);
    }
}
