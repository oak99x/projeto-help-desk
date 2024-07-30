package mfreitas.msuser.ControllersTests;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import mfreitas.msuser.dtos.UserLoginDTO;
import mfreitas.msuser.services.LoginService;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService authService;

    @Test
    public void testGetUserByEmail() throws Exception {
        String email = "john.doe@example.com";
        
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setEmail(email);
        userLoginDTO.setName("John Doe");

        when(authService.getUserByEmail(anyString())).thenReturn(userLoginDTO);

        mockMvc.perform(get("/api/login/{email}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(authService, times(1)).getUserByEmail(email);
    }
}
