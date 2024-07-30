package mfreitas.msuser.ControllersTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import mfreitas.msuser.dtos.UpdateUserDTO;
import mfreitas.msuser.dtos.UserDTO;
import mfreitas.msuser.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testGetUserById() throws Exception {
        UUID userId = UUID.randomUUID();

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setName("John Doe");

        when(userService.getUserById(userId)).thenReturn(userDTO);

        mockMvc.perform(get("/api/user")
                .header("X-User-Id", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        String email = "john.doe@example.com";
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setName("John Doe");

        when(userService.getUserByEmail(email)).thenReturn(userDTO);

        mockMvc.perform(get("/api/user/email/{email}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(userService, times(1)).getUserByEmail(email);
    }

    @Test
    public void testUpdateUser() throws Exception {
        UUID userId = UUID.randomUUID();

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setName("Updated Name");
        updateUserDTO.setEmail("updatedemail@gmail.com");

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(userId);
        updatedUserDTO.setName("Updated Name");
        updatedUserDTO.setEmail("updatedemail@gmail.com");

        when(userService.updateUser(eq(userId), any(UpdateUserDTO.class))).thenReturn(updatedUserDTO);

        mockMvc.perform(put("/api/user/update")
                .header("X-User-Id", userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updatedemail@gmail.com"));

        verify(userService, times(1)).updateUser(eq(userId), any(UpdateUserDTO.class));
    }
}
