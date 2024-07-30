package mfreitas.msuser.RepositoriesTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import mfreitas.msuser.models.Role;
import mfreitas.msuser.models.User;
import mfreitas.msuser.repositories.RoleRepository;
import mfreitas.msuser.repositories.UserRepository;

@SpringBootTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DirtiesContext
    public void testFindByNameContainingIgnoreCase() {
        // Criação de usuários de exemplo
        User user1 = new User(null, "John Doe", "john.doe@example.com", "password", null);
        User user2 = new User(null, "Jane Smith", "jane.smith@example.com", "password", null);
        userRepository.save(user1);
        userRepository.save(user2);

        // Teste de busca por nome
        List<User> foundUsers = userRepository.findByNameContainingIgnoreCase("doe");
        assertEquals(1, foundUsers.size());
        assertEquals("John Doe", foundUsers.get(0).getName());
    }

    @Test
    @DirtiesContext
    public void testFindByEmailContainingIgnoreCase() {
        // Criação de usuários de exemplo
        User user1 = new User(null, "John Doe", "john.doe@example.com", "password", null);
        User user2 = new User(null, "Jane Smith", "jane.smith@example.com", "password", null);
        userRepository.save(user1);
        userRepository.save(user2);

        // Teste de busca por email
        List<User> foundUsers = userRepository.findByEmailContainingIgnoreCase("example");
        assertEquals(2, foundUsers.size());
    }

    @Test
    @DirtiesContext
    public void testFindByEmailIgnoreCase() {
        // Criação de usuário de exemplo
        User user = new User(null, "John Doe", "john.doe@example.com", "password", null);
        userRepository.save(user);

        // Teste de busca por email
        Optional<User> foundUser = userRepository.findByEmailIgnoreCase("john.doe@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getName());
    }

    @Test
    @DirtiesContext
    public void testFindByRoles_RoleNameIgnoreCase() {
        // Criação de usuário de exemplo com role
        Role role = new Role();
        role.setRoleName("ROLE_ADMIN");
        Role savedRole = roleRepository.save(role);

        User user = new User(null, "John Doe", "john.doe@example.com", "password", new HashSet<>());
        user.addRole(savedRole);
        userRepository.save(user);

        // Teste de busca por roleName
        List<User> foundUsers = userRepository.findByRoles_RoleNameIgnoreCase("role_admin");
        assertEquals(1, foundUsers.size());
        assertEquals("John Doe", foundUsers.get(0).getName());
    }
}
