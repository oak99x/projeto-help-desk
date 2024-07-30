package mfreitas.msticket.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.validation.Valid;
import mfreitas.msticket.dtos.ProcedureCreateDTO;
import mfreitas.msticket.dtos.ProcedureDTO;
import mfreitas.msticket.dtos.TicketCreateDTO;
import mfreitas.msticket.dtos.TicketDTO;
import mfreitas.msticket.dtos.TicketFilter;
import mfreitas.msticket.dtos.UpdateTicketDTO;
import mfreitas.msticket.services.TicketService;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/create")
    public ResponseEntity<TicketDTO> createNewTicket(@RequestBody @Valid TicketCreateDTO ticketDTO) {
        TicketDTO newTicket = ticketService.createNewTicket(ticketDTO);
        return new ResponseEntity<>(newTicket, HttpStatus.CREATED);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable String ticketId) {
        TicketDTO ticketDTO = ticketService.getTicketById(ticketId);
        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TicketDTO>> filterTickets(HttpServletRequest request,
            @RequestParam(required = false) boolean all,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String clientEmail,
            @RequestParam(required = false) String supportEmail,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        String userMode = request.getHeader("X-User-Mode");
        TicketFilter ticketFilter = new TicketFilter(all, title, department, status, clientEmail, supportEmail, startDate, endDate);
        List<TicketDTO> ticketList = ticketService.filterTickets(ticketFilter, userMode);
        return new ResponseEntity<>(ticketList, HttpStatus.OK);
    }

    //Ainda haver se serão usados-------------------------------------------------------------------------------
    // @GetMapping("/user-tickets")
    // public ResponseEntity<List<TicketDTO>> getAllUserTickets(HttpServletRequest request) {
    //     String userMode = request.getHeader("X-User-Mode");
    //     List<TicketDTO> ticketList = ticketService.getAllUserTickets(userMode);
    //     return new ResponseEntity<>(ticketList, HttpStatus.OK);
    // }

    // @GetMapping("/status")
    // public ResponseEntity<List<TicketDTO>> getTicketByStatus(String status) {
    //     List<TicketDTO> ticketList = ticketService.getTicketByStatus(status);
    //     return new ResponseEntity<>(ticketList, HttpStatus.OK);
    // }
    //------------------------------------------------------------------------------------------------------

    @PutMapping("/assign/{ticketId}")
    public ResponseEntity<TicketDTO> assignTicketToAdmin(@PathVariable String ticketId) {
        TicketDTO ticketDTO = ticketService.assignTicketToAdmin(ticketId);
        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @PutMapping("/{ticketId}/update")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable String ticketId,
    @RequestBody UpdateTicketDTO updateTicketDTO) {
        TicketDTO ticketDTO = ticketService.updateTicket(ticketId, updateTicketDTO);
        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @PutMapping("/{ticketId}/concluded")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable String ticketId) {
        TicketDTO ticketDTO = ticketService.concludedTicket(ticketId);
        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @PostMapping("/{ticketId}/respond")
    public ResponseEntity<TicketDTO> respondToTicket(HttpServletRequest request, @PathVariable String ticketId,
            @RequestBody @Valid ProcedureCreateDTO procedure) {
        String userMode = request.getHeader("X-User-Mode");
        TicketDTO ticketDTO = ticketService.respondToTicket(ticketId, procedure, userMode);
        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @GetMapping("/{ticketId}/procedures")
    public ResponseEntity<List<ProcedureDTO>> respondToTicket(@PathVariable String ticketId) {
        List<ProcedureDTO> procedures = ticketService.searchProcedures(ticketId);
        if (procedures != null) {
            return new ResponseEntity<>(procedures, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{ticketId}")
    public ResponseEntity<TicketDTO> deleteTicket(@PathVariable String ticketId) {
        TicketDTO ticketDTO = ticketService.deleteTicket(ticketId);
        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

}
