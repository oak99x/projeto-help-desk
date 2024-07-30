package mfreitas.msoauth.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AuthRequest {

    @NotEmpty(message = "Campo email obrigatorio")
    private String userEmail;
    @NotEmpty(message = "Campo password obrigatorio")
    private String password;

}
