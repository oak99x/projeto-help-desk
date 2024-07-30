package mfreitas.msuser.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserFilter {
    // private boolean all;
    private String area;
    private String role;
    private String userEmail;
}