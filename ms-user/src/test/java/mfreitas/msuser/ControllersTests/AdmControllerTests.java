package mfreitas.msuser.ControllersTests;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasSize;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import mfreitas.msuser.dtos.UserDTO;
import mfreitas.msuser.dtos.UserFilter;
import mfreitas.msuser.models.Role;
import mfreitas.msuser.services.AdmService;

@SpringBootTest
@AutoConfigureMockMvc
public class AdmControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdmService admService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllUsers() throws Exception {
        List<UserDTO> users = new ArrayList<>();
        users.add(new UserDTO());

        when(admService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(users.size()));

        verify(admService, times(1)).getAllUsers();
    }

    @Test
    public void testGetUsersByName() throws Exception {
        String name = "John";

        UserDTO userDTO = new UserDTO();
        userDTO.setName("John Doe");

        List<UserDTO> users = new ArrayList<>();
        users.add(userDTO);

        when(admService.getUsersByName(name)).thenReturn(users);

        mockMvc.perform(get("/api/admin/users/name/{name}", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(users.size()))
                .andExpect(jsonPath("$[0].name", containsString(name)));

        verify(admService, times(1)).getUsersByName(name);
    }

    @Test
    public void testGetUsersByEmail() throws Exception {
        String email = "john.doe@example.com";
        List<UserDTO> users = new ArrayList<>();
        users.add(new UserDTO());

        when(admService.getUsersByEmail(email)).thenReturn(users);

        mockMvc.perform(get("/api/admin/users/email/{email}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(users.size()));

        verify(admService, times(1)).getUsersByEmail(email);
    }

    @Test
    public void testGetUsersByIncompleteEmail() throws Exception {
        String email = "john.doe";
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        List<UserDTO> users = new ArrayList<>();
        users.add(userDTO);

        when(admService.getUsersByEmail(email)).thenReturn(users);

        mockMvc.perform(get("/api/admin/users/email/{email}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(users.size()))
                .andExpect(jsonPath("$[0].email", containsString(email)));

        verify(admService, times(1)).getUsersByEmail(email);
    }

    @Test
    public void testGetUsersByRole() throws Exception {
        String role = "ROLE_ADMIN";
        List<UserDTO> users = new ArrayList<>();
        users.add(new UserDTO());

        when(admService.getUsersByRole(role)).thenReturn(users);

        mockMvc.perform(get("/api/admin/users/role/{role}", role))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(users.size()));

        verify(admService, times(1)).getUsersByRole(role);
    }

    @Test
    public void testGetUsersByRoleIgnoreCase() throws Exception {
        String role = "role_admin";

        Role adminRole = new Role();
        adminRole.setId(UUID.randomUUID());
        adminRole.setRoleName("ROLE_ADMIN");

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        
        UserDTO userDTO = new UserDTO();
        userDTO.setRoles(roles);

        List<UserDTO> users = new ArrayList<>();
        users.add(userDTO);

        when(admService.getUsersByRole(role)).thenReturn(users);

        mockMvc.perform(get("/api/admin/users/role/{role}", role))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(users.size()))
                .andExpect(jsonPath("$[0].roles", hasSize(1)))
                .andExpect(jsonPath("$[0].roles[0].roleName", equalToIgnoringCase(role)));

        verify(admService, times(1)).getUsersByRole(role);
    }

    @Test
    public void testAddUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("john.doe@example.com");

        when(admService.addUser(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/api/admin/add-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));

        verify(admService, times(1)).addUser(any(UserDTO.class));
    }

    @Test
    public void testPromoteToAdmin() throws Exception {
        String email = "john.doe@example.com";
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);

        when(admService.promoteToAdmin(email)).thenReturn(userDTO);

        mockMvc.perform(put("/api/admin/promoteToAdmin/{email}", email))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.email").value(email));

        verify(admService, times(1)).promoteToAdmin(email);
    }

    @Test
    public void testDeleteRoleAdmin() throws Exception {
        String email = "john.doe@example.com";
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);

        when(admService.deleteRoleAdmin(email)).thenReturn(userDTO);

        mockMvc.perform(delete("/api/admin/deleteRoleAdmin/{email}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));

        verify(admService, times(1)).deleteRoleAdmin(email);
    }

     @Test
    public void testFilterTickets() throws Exception {
        String role = "ROLE_ADMIN";
        String userEmail = "user@example.com";

        UserFilter userFilter = new UserFilter(null,role, userEmail);
        
        List<UserDTO> users = new ArrayList<>();
        users.add(new UserDTO());

        when(admService.filterUsers(userFilter)).thenReturn(users);

        mockMvc.perform(get("/api/admin/filter")
                .param("role", role)
                .param("userEmail", userEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(users.size()));

        verify(admService, times(1)).filterUsers(any(UserFilter.class));
    }
}