package mfreitas.msticket.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import mfreitas.msticket.models.User;
import mfreitas.msticket.resources.enums.Status;

@Data
public class TicketDTO{
    private String id;
    private String title;
    private String description;
    private String department;
    private User generator;
    private User client;
    private User support;
    private Status status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    //private List<ProcedureDTO> procedures = new ArrayList<>();
}
