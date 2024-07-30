package mfreitas.msticket.dtos;

import java.time.LocalDateTime;

import org.springframework.validation.annotation.Validated;

import lombok.Data;
import mfreitas.msticket.models.User;

@Data
@Validated
public class ProcedureDTO {
    private String content;
    private User sender;
    private LocalDateTime dateTime;
}
