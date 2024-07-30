package mfreitas.msuser.DtosTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import mfreitas.msuser.dtos.UserFilter;

public class UserFilterTests {
    @Test
    public void testNoArgsConstructor() {
        UserFilter filter = new UserFilter();
        assertNotNull(filter);
    }

    @Test
    public void testAllArgsConstructor() {
        String area = "Test Area";
        String role = "ROLE_ADMIN";
        String userEmail = "test@example.com";

        UserFilter filter = new UserFilter(area, role, userEmail);

        assertEquals(area, filter.getArea());
        assertEquals(role, filter.getRole());
        assertEquals(userEmail, filter.getUserEmail());
    }

    @Test
    public void testGettersAndSetters() {
        UserFilter filter = new UserFilter();

        String area = "Test Area";
        String role = "ROLE_ADMIN";
        String userEmail = "test@example.com";

        filter.setArea(area);
        filter.setRole(role);
        filter.setUserEmail(userEmail);

        assertEquals(area, filter.getArea());
        assertEquals(role, filter.getRole());
        assertEquals(userEmail, filter.getUserEmail());
    }
}
