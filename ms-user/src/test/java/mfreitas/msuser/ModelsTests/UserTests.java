package mfreitas.msuser.ModelsTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import mfreitas.msuser.models.Role;
import mfreitas.msuser.models.User;

@SpringBootTest
public class UserTests {

    @Test
    public void testUserConstructionAndFieldAssignment() {
        // Criação de um usuário de exemplo
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        // Teste de getters
        assertEquals(userId, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testAddRoleMethod() {
        // Criação de um usuário de exemplo
        User user = new User();
        Role role = new Role();
        UUID roleId = UUID.randomUUID();
        role.setId(roleId);
        role.setRoleName("ROLE_USER");

        // Adicionando papel ao usuário
        user.addRole(role);

        // Verificação se o papel foi adicionado corretamente
        assertEquals(1, user.getRoles().size());
        assertEquals("ROLE_USER", user.getRoles().iterator().next().getRoleName());
    }

    @Test
    public void testHasRoleMethod() {
        // Criação de um usuário de exemplo
        User user = new User();
        Role role = new Role();
        role.setRoleName("ROLE_USER");

        // Adicionando papel ao usuário
        user.addRole(role);

        // Testando o método hasRole
        assertTrue(user.hasRole("ROLE_USER"));
        assertFalse(user.hasRole("ROLE_ADMIN"));
    }

    @Test
    public void testEqualityAndHashCode() {
        // Criação de dois usuários com o mesmo ID
        User user1 = new User();
        UUID userId = UUID.randomUUID();
        user1.setId(userId);
        User user2 = new User();
        user2.setId(userId);

        // Verificação de igualdade e desigualdade
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());

        // Modificação de um campo para verificar desigualdade
        user2.setEmail("another.email@example.com");
        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }
    
}
