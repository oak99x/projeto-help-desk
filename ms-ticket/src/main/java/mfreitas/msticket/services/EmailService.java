package mfreitas.msticket.services;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mfreitas.msticket.feignClients.EmailFeign;
import mfreitas.msticket.models.Email;
import mfreitas.msticket.models.Ticket;

@Service
public class EmailService {

    @Autowired
    private EmailFeign emailFeign;

    public void sendNewTicket(Ticket t) {
        Email email = configureEmail(t, "New Ticket");

        email.setDescription(t.getDescription());
        emailFeign.sendEmail(email);
    }
    
    public void sendNewProcedure(Ticket t) {
        Email email = configureEmail(t, "New Procedure");
    
        //pega a ultima mensagem
        int index = t.getProcedures().size() - 1;
        email.setDescription(t.getProcedures().get(index).getContent());
    
        emailFeign.sendEmail(email);
    }
    
    private Email configureEmail(Ticket t, String procedureType) {
        Email email = new Email();
    
        email.setToUser(t.getClient());
        email.setTitle(t.getTitle());
        email.setProcedure(procedureType);
    
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        
        String startDate = t.getStartDate().format(formatter);
        email.setStartDate(startDate);

        String endDate = t.getEndDate() != null? t.getEndDate().format(formatter) : "-";
        email.setEndDate(endDate);
    
        email.setGenerator(t.getGenerator().getName());
        email.setRequester(t.getClient().getName());
    
        if (t.getSupport() == null) {
            email.setResponsible("-");
        } else {
            email.setResponsible(t.getSupport().getName());
        }
    
        email.setStatus(t.getStatus().toString());
    
        // Pegar os 4 últimos caracteres da ID
        int length = t.getId().length();
        email.setTicketId(t.getId().substring(length - 4));
    
        return email;
    }
    
}
