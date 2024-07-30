package mfreitas.msticket.services;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import mfreitas.msticket.dtos.TicketFilter;
import mfreitas.msticket.exceptions.ObjectNotFoundException;
import mfreitas.msticket.feignClients.UserFeign;
import mfreitas.msticket.models.User;
import mfreitas.msticket.resources.enums.Status;

@Service
public class SearchTickets {

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private MessageSource messageSource;

    public Query filter(TicketFilter filter, String userMode) {       
        Query query = new Query();

        if (StringUtils.hasText(filter.getTitle())) {
            query.addCriteria(Criteria.where("title").regex(".*" + Pattern.quote(filter.getTitle()) + ".*", "i"));
        }
    
    
        if (StringUtils.hasText(filter.getDepartment())) {
            query.addCriteria(Criteria.where("department").regex(filter.getDepartment(), "i"));
        }
    
        // Converter a String de status para o enum Status
        if (StringUtils.hasText(filter.getStatus())) {
            try {
                Status status = Status.valueOf(filter.getStatus().toUpperCase());
                query.addCriteria(Criteria.where("status").is(status));
            } catch (IllegalArgumentException e) {
                throw new ObjectNotFoundException(messageSource.getMessage("status.not.found", null, null) + filter.getStatus());
            }
        }
    
        //Se estiver no modo de usuario comum, no filtro será obrigado a trazer só os dados que lhe pertencem
        if ("USER".equalsIgnoreCase(userMode)) {
            User currentUser = userFeign.getUserById().getBody();
            query.addCriteria(Criteria.where("client").is(currentUser));
        }else{
            if (StringUtils.hasText(filter.getClientEmail())) {
                User client = userFeign.getUserByEmail(filter.getClientEmail()).getBody();
                query.addCriteria(Criteria.where("client").is(client));
            }
        }

        //Se estiver no modo admin e filter allTickets for false, traz só os tickets daquele suport
        //Se filter allTickets for true, pula e traz todos os tickets
        if ("ADMIN".equalsIgnoreCase(userMode) && !filter.isAll()) {
            User currentUser = userFeign.getUserById().getBody();
            query.addCriteria(Criteria.where("support").is(currentUser));   
        }

        // Filtando por outro support
        if (StringUtils.hasText(filter.getSupportEmail())) {
            User support = userFeign.getUserByEmail(filter.getSupportEmail()).getBody();
            query.addCriteria(Criteria.where("support").is(support));
        }

        // Converter LocalDate para LocalDateTime para fazer a comparação de datas
        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            LocalDateTime startDateTime = filter.getStartDate().atStartOfDay(); // Início do dia para a data de início
            LocalDateTime endDateTime = filter.getEndDate().atTime(LocalTime.MAX); // Fim do dia para a data de término

            query.addCriteria(Criteria.where("startDate").gte(startDateTime).lte(endDateTime));
        } else if (filter.getStartDate() != null) {
            LocalDateTime startDateTime = filter.getStartDate().atStartOfDay();
            query.addCriteria(Criteria.where("startDate").gte(startDateTime));
        } else if (filter.getEndDate() != null) {
            LocalDateTime endDateTime = filter.getEndDate().atTime(LocalTime.MAX);
            query.addCriteria(Criteria.where("startDate").lte(endDateTime));
        }
    
        return query;
    }
}
