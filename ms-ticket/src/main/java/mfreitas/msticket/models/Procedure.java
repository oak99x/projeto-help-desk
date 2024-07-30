package mfreitas.msticket.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "procedures")
public class Procedure implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String content;
    private User sender;
    private LocalDateTime dateTime;
}
