package mfreitas.msticket.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketFilter {
    private boolean all;
    private String title;
    private String department;
    private String status;
    private String clientEmail;
    private String supportEmail;
    private LocalDate startDate;
    private LocalDate endDate;
}
