package mfreitas.msemail.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Email {
    @NotNull(message = "{toUser.not.null}")
    private User toUser;

    private String name;

    @NotEmpty(message = "{procedure.not.empty}")
    private String procedure;

    @NotEmpty(message = "{date.not.empty}")
    private String startDate;

    @NotEmpty(message = "{date.not.empty}")
    private String endDate;

    @NotEmpty(message = "{generator.not.empty}")
    private String generator;

    @NotEmpty(message = "{requester.not.empty}")
    private String requester;

    @NotEmpty(message = "{responsible.not.empty}")
    private String responsible;

    @NotEmpty(message = "{status.not.empty}")
    private String status;

    @NotEmpty(message = "{ticketId.not.empty}")
    private String ticketId;

    @NotEmpty(message = "{title.not.empty}")
    private String title;

    @NotEmpty(message = "{description.not.empty}")
    private String description;
}
