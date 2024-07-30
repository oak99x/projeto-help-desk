package mfreitas.msuser.ServicesTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import mfreitas.msuser.dtos.UserDTO;
import mfreitas.msuser.dtos.UserFilter;
import mfreitas.msuser.models.Role;
import mfreitas.msuser.models.User;
import mfreitas.msuser.repositories.UserRepository;
import mfreitas.msuser.resources.userMappers.UserMapper;
import mfreitas.msuser.services.SearchUsers;

@SpringBootTest
public class SearchUsersTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private SearchUsers searchUsers;

    private UserFilter userFilter;

    @BeforeEach
    public void setUp() {
        userFilter = new UserFilter();
    }

    @Test
    public void shouldReturnNotNullForSearchUsersFilter() {
       
        // Definindo o filtro
        userFilter.setUserEmail("test@gmail.com");
        userFilter.setRole("user");

        // Executando o método a ser testado
        Specification<User> spec = searchUsers.filter(userFilter);

        assertNotNull(spec);
    }
}
