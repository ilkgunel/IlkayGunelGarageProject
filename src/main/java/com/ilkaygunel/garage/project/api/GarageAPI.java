package com.ilkaygunel.garage.project.api;

import com.ilkaygunel.garage.project.model.CreateTicketRequest;
import com.ilkaygunel.garage.project.model.CreateTicketResponse;
import com.ilkaygunel.garage.project.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GarageAPI {

    @Autowired
    private TicketService ticketService;

    @RequestMapping(method = RequestMethod.GET, value = "/status")
    public ResponseEntity<?> status() {
        return new ResponseEntity<>(ticketService.prepareAndGetStatusReport(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/park")
    public ResponseEntity<CreateTicketResponse> createTicket(@RequestBody CreateTicketRequest createTicketRequest) throws Exception {
        CreateTicketResponse createTicketResponse = ticketService.createTicketForIncomingVehicle(createTicketRequest);
        return new ResponseEntity<>(createTicketResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/leave/{ticketId}")
    public ResponseEntity<?> leaveCarFromGarage(@PathVariable Long ticketId) {
        ticketService.leaveCarFromGarage(ticketId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
