package mfreitas.msticket.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Email {
    private User toUser;
    private String name;
    private String procedure;
    private String startDate;
    private String endDate;
    private String generator;
    private String requester;
    private String responsible;
    private String status;
    private String ticketId;
    private String title;
    private String description;
}
