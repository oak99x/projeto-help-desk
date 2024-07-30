package mfreitas.msticket.resources.ticketMappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import mfreitas.msticket.dtos.TicketCreateDTO;
import mfreitas.msticket.dtos.TicketDTO;
import mfreitas.msticket.models.Ticket;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    TicketDTO ticketToTicketDTO(Ticket ticket);

    // Ticket ticketDTOToTicket(TicketDTO ticketDTO);

    Ticket ticketCreateDTOToTicket(TicketCreateDTO ticketCreateDTO);

    List<TicketDTO> listTicketToTicketDTO(List<Ticket> ticketList);

}
