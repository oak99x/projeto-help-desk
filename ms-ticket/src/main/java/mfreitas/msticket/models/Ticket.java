package mfreitas.msticket.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mfreitas.msticket.resources.enums.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tickets")
public class Ticket implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String title;
    private String description;
    private String department;
    private User generator;
    private User client;
    private User support;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Status status;

    @DocumentReference(lazy = true)   
    private List<Procedure> procedures = new ArrayList<>();

}
