package mfreitas.msticket.dtos;

import lombok.Data;

@Data
public class UpdateTicketDTO{
    private String department;
    private String status;
}