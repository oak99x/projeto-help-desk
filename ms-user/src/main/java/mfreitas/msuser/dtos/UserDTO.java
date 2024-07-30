package mfreitas.msuser.dtos;

import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import mfreitas.msuser.models.Role;

@Data
public class UserDTO {
    
    private UUID id;
    @NotEmpty(message = "{email.not.found}")
    @Email(message = "{invalid.email}")
    private String email;
    private String name;
    private Set<Role> roles;
}
