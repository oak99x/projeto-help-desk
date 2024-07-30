package mfreitas.msticket.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TicketCreateDTO{

    @NotEmpty(message = "{invalid.title}")
    private String title;
    @NotEmpty(message = "{invalid.description}")
    private String description;
    @NotEmpty(message = "{invalid.department}")
    private String department;
    private String clientEmail;
}
