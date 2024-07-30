package mfreitas.msuser.ModelsTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import mfreitas.msuser.models.Role;

@SpringBootTest
public class RoleTests {
    @Test
    public void testRoleConstructionAndFieldAssignment() {
        // Criação de um papel de exemplo
        Role role = new Role();
        UUID roleId = UUID.randomUUID();
        role.setId(roleId);
        role.setRoleName("ROLE_ADMIN");

        // Teste de getters
        assertEquals(roleId, role.getId());
        assertEquals("ROLE_ADMIN", role.getRoleName());
    }

    @Test
    public void testEqualityAndHashCode() {
        // Criação de dois papéis com o mesmo ID
        Role role1 = new Role();
        UUID roleId = UUID.randomUUID();
        role1.setId(roleId);
        Role role2 = new Role();
        role2.setId(roleId);

        // Verificação de igualdade e desigualdade
        assertEquals(role1, role2);
        assertEquals(role1.hashCode(), role2.hashCode());

        // Modificação de um campo para verificar desigualdade
        role2.setRoleName("ROLE_USER");
        assertNotEquals(role1, role2);
        assertNotEquals(role1.hashCode(), role2.hashCode());
    }
}
