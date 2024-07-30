package mfreitas.msticket.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import mfreitas.msticket.models.Ticket;
import mfreitas.msticket.models.User;
import mfreitas.msticket.resources.enums.Status;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String>{
    // Método para encontrar todos os tickets associados a um cliente específico
    List<Ticket> findByClient(User client);
    // Método para encontrar todos os tickets associados a um suporte específico
    List<Ticket> findBySupport(User support);

    List<Ticket> findByStatus(Status status);
}
