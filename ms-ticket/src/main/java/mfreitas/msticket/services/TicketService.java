package mfreitas.msticket.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import feign.FeignException;
import mfreitas.msticket.dtos.ProcedureCreateDTO;
import mfreitas.msticket.dtos.ProcedureDTO;
import mfreitas.msticket.dtos.TicketCreateDTO;
import mfreitas.msticket.dtos.TicketDTO;
import mfreitas.msticket.dtos.TicketFilter;
import mfreitas.msticket.dtos.UpdateTicketDTO;
import mfreitas.msticket.exceptions.FailedToRespondException;
import mfreitas.msticket.exceptions.ObjectNotFoundException;
import mfreitas.msticket.feignClients.UserFeign;
import mfreitas.msticket.models.Procedure;
import mfreitas.msticket.models.Ticket;
import mfreitas.msticket.models.User;
import mfreitas.msticket.repositories.ProcedureRepository;
import mfreitas.msticket.repositories.TicketRepository;
import mfreitas.msticket.resources.enums.Status;
import mfreitas.msticket.resources.ticketMappers.ProcedureMapper;
import mfreitas.msticket.resources.ticketMappers.TicketMapper;

@Service
public class TicketService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProcedureRepository procedureRepository;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SearchTickets searchTickets;

    public TicketDTO createNewTicket(TicketCreateDTO ticketDTO) {
        try {
            Ticket newTicket = TicketMapper.INSTANCE.ticketCreateDTOToTicket(ticketDTO);

            User generator = userFeign.getUserById().getBody();

            newTicket.setGenerator(generator);

            if (ticketDTO.getClientEmail() != null) {
                User client = userFeign.getUserByEmail(ticketDTO.getClientEmail()).getBody();
                newTicket.setClient(client);
            } else {
                newTicket.setClient(generator);
            }

            newTicket.setStartDate(LocalDateTime.now());
            newTicket.setStatus(Status.OPEN);
            ticketRepository.save(newTicket);

            // post email
            emailService.sendNewTicket(newTicket);

            return TicketMapper.INSTANCE.ticketToTicketDTO(newTicket);
        } catch (FeignException.NotFound ex) {
            throw new ObjectNotFoundException(
                    messageSource.getMessage("user.not.found", null, null) + ex.getMessage());
        } catch (Exception ex) {
            throw new FailedToRespondException(
                    messageSource.getMessage("failed.to.respond", null, null) + ex.getMessage());
        }
    }

    public TicketDTO getTicketById(String ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(
                        () -> new ObjectNotFoundException(
                                messageSource.getMessage("ticket.not.found", null, null) + ticketId));

        return TicketMapper.INSTANCE.ticketToTicketDTO(ticket);
    }

    public List<TicketDTO> filterTickets(TicketFilter ticketFilter, String userMode) {
        List<Ticket> tickets = mongoTemplate.find(searchTickets.filter(ticketFilter, userMode), Ticket.class);
        return TicketMapper.INSTANCE.listTicketToTicketDTO(tickets);
    }

    //-----------------------------------------------------------------------------------------------------
    // public List<TicketDTO> getAllUserTickets(String userMode) {
    //     User user = userFeign.getUserById().getBody();

    //     List<Ticket> ticketList = new ArrayList<>();

    //     if ("ADMIN".equalsIgnoreCase(userMode)) {
    //         ticketList = ticketRepository.findBySupport(user);
    //     } else {
    //         ticketList = ticketRepository.findByClient(user);
    //     }
    //     return TicketMapper.INSTANCE.listTicketToTicketDTO(ticketList);
    // }

    // public List<TicketDTO> getTicketByStatus(String status) {
    //     List<Ticket> tickets = ticketRepository.findByStatus(Status.valueOf(status.toUpperCase()));
    //     return TicketMapper.INSTANCE.listTicketToTicketDTO(tickets);
    // }
    //-----------------------------------------------------------------------------------------------------

    public TicketDTO assignTicketToAdmin(String ticketId) {
        try {
            Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ObjectNotFoundException(
                    messageSource.getMessage("ticket.not.found", null, null) + ticketId));

            // chama api user
            User support = userFeign.getUserById().getBody();

            ticket.setSupport(support);
            ticket.setStatus(Status.IN_PROGRESS);

            ticketRepository.save(ticket);
            return TicketMapper.INSTANCE.ticketToTicketDTO(ticket);

        } catch (ObjectNotFoundException ex) {
            throw new ObjectNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            throw new FailedToRespondException(messageSource.getMessage("failed.to.respond", null, null));
        }
    }

    public TicketDTO updateTicket(String ticketId, UpdateTicketDTO updateTicketDTO) {
        try {
            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new ObjectNotFoundException(
                            messageSource.getMessage("ticket.not.found", null, null) + ticketId));

            if(updateTicketDTO.getStatus() != null){
                ticket.setStatus(Status.valueOf(updateTicketDTO.getStatus().toUpperCase()));
            }

            if(updateTicketDTO.getDepartment() != null){
                ticket.setDepartment(updateTicketDTO.getDepartment().toUpperCase());
            }

            ticketRepository.save(ticket);
            return TicketMapper.INSTANCE.ticketToTicketDTO(ticket);

        } catch (ObjectNotFoundException ex) {
            throw new ObjectNotFoundException(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new ObjectNotFoundException(messageSource.getMessage("status.not.found", null, null) + updateTicketDTO.getStatus());
        } catch (Exception ex) {
            throw new FailedToRespondException(
                    messageSource.getMessage("failed.to.respond", null, null) + ex.getMessage());
        }
    }

    public TicketDTO concludedTicket(String ticketId) {
        try {
            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new ObjectNotFoundException(
                            messageSource.getMessage("ticket.not.found", null, null) + ticketId));

            ticket.setStatus(Status.CONCLUDED);
            ticket.setEndDate(LocalDateTime.now());
           
            ticketRepository.save(ticket);
            return TicketMapper.INSTANCE.ticketToTicketDTO(ticket);

        } catch (ObjectNotFoundException ex) {
            throw new ObjectNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            throw new FailedToRespondException(
                    messageSource.getMessage("failed.to.respond", null, null) + ex.getMessage());
        }
    }

    

    public TicketDTO respondToTicket(String ticketId, ProcedureCreateDTO procedureDto, String userMode) {
        try {
            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new ObjectNotFoundException(
                            messageSource.getMessage("ticket.not.found", null, null) + ticketId));

            // pegando user atual
            User sender = userFeign.getUserById().getBody();

            Procedure newProcedure = new Procedure();

            newProcedure.setContent(procedureDto.getContent());
            newProcedure.setSender(sender);
            newProcedure.setDateTime(LocalDateTime.now());

            newProcedure = procedureRepository.save(newProcedure);

            ticket.getProcedures().add(newProcedure);
            ticket = ticketRepository.save(ticket);

            // envia email só support -> user
            if ("ADMIN".equalsIgnoreCase(userMode)) {
                emailService.sendNewProcedure(ticket);
            }

            return TicketMapper.INSTANCE.ticketToTicketDTO(ticket);
        } catch (FeignException.NotFound ex) {
            throw new ObjectNotFoundException(
                    messageSource.getMessage("user.not.found", null, null) + ex.getMessage());
        } catch (ObjectNotFoundException ex) {
            throw new ObjectNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            throw new FailedToRespondException(
                    messageSource.getMessage("failed.to.respond", null, null) + ex.getMessage());
        }
    }

    public List<ProcedureDTO> searchProcedures(String ticketId) {
        try {
            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new ObjectNotFoundException(
                            messageSource.getMessage("ticket.not.found", null, null) + ticketId));

            return ProcedureMapper.INSTANCE.listProceduresToProcedureDTOs(ticket.getProcedures());

        } catch (ObjectNotFoundException ex) {
            throw new ObjectNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            throw new FailedToRespondException(
                    messageSource.getMessage("failed.to.respond", null, null) + ex.getMessage());
        }
    }

    public TicketDTO deleteTicket(String ticketId) {
        try {
            Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ObjectNotFoundException(
                    messageSource.getMessage("ticket.not.found", null, null) + ticketId));

            // Primeiro, deleta todas as procedures associadas ao ticket
            procedureRepository.deleteAll(ticket.getProcedures());
            
            ticketRepository.deleteById(ticketId);

            return TicketMapper.INSTANCE.ticketToTicketDTO(ticket);
        } catch (ObjectNotFoundException ex) {
            throw new ObjectNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            throw new FailedToRespondException(messageSource.getMessage("failed.to.respond", null, null));
        }
    }
}
