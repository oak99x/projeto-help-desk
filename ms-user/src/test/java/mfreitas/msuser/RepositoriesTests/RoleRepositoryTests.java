package mfreitas.msuser.RepositoriesTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import mfreitas.msuser.models.Role;
import mfreitas.msuser.repositories.RoleRepository;

@SpringBootTest
public class RoleRepositoryTests {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DirtiesContext
    public void testSaveRole() {
        // Criação de um novo papel
        Role role = new Role();
        role.setRoleName("ROLE_ADMIN");

        // Salvando no repositório
        Role savedRole = roleRepository.save(role);

        // Verificação se foi salvo corretamente
        assertNotNull(savedRole.getId());
        assertEquals("ROLE_ADMIN", savedRole.getRoleName());

        // Buscando o papel pelo ID salvo
        Optional<Role> retrievedRole = roleRepository.findById(savedRole.getId());

        // Verificação adicional para garantir que o papel foi encontrado no banco de dados
        assertTrue(retrievedRole.isPresent(), "A role salva deve ser encontrada no banco de dados.");
        assertEquals(savedRole.getId(), retrievedRole.get().getId(), "O ID recuperado deve ser o mesmo que o ID salvo.");
        assertEquals("ROLE_ADMIN", retrievedRole.get().getRoleName(), "O nome da role recuperada deve ser ROLE_ADMIN.");
    }

    @Test
    @DirtiesContext
    public void testFindByRoleNameIgnoreCase() {
        
        Role role = new Role();
        role.setRoleName("ROLE_USER");

        Role savedRole = roleRepository.save(role);

        // Busca pelo nome do papel ignorando maiúsculas/minúsculas
        Optional<Role> foundRole = roleRepository.findByRoleNameIgnoreCase("role_user");

        // Verifica se o papel foi encontrado
        assertTrue(foundRole.isPresent());
        assertEquals("ROLE_USER", foundRole.get().getRoleName());
    }

    @Test
    @DirtiesContext
    public void testFindByRoleNameIgnoreCase_NotFound() {
        // Busca por um papel que não existe
        Optional<Role> foundRole = roleRepository.findByRoleNameIgnoreCase("role_that_does_not_exist");

        // Verifica se nenhum papel foi encontrado
        assertFalse(foundRole.isPresent());
    }
}
