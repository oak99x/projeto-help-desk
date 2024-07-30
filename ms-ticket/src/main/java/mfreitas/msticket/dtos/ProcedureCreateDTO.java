package mfreitas.msticket.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Validated
public class ProcedureCreateDTO {
    @NotEmpty(message = "{invalid.content}")
    private String content;
}
