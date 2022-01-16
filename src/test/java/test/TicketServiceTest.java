package test;

import com.ilkaygunel.garage.project.exceptionhandling.NoLeftSlotException;
import com.ilkaygunel.garage.project.model.CreateTicketRequest;
import com.ilkaygunel.garage.project.model.CreateTicketResponse;
import com.ilkaygunel.garage.project.service.TicketService;
import com.ilkaygunel.garage.project.singleton.VehicleSlotsSingleton;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketService.class)
public class TicketServiceTest {

    @InjectMocks
    TicketService ticketService;

    @Value("${initialSlotRange}")
    private Integer initialSlotRange;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(ticketService, "initialSlotRange", initialSlotRange);
    }

    @Test
    public void createTicketForCarTest() throws Exception {
        CreateTicketRequest createTicketRequest = new CreateTicketRequest();
        createTicketRequest.setPlate("34-SO-1988");
        createTicketRequest.setColor("Black");
        createTicketRequest.setVehicleType("Car");

        CreateTicketResponse createTicketResponse = ticketService.createTicketForIncomingVehicle(createTicketRequest);

        assertNotNull(createTicketResponse.getTicketId());
        assertEquals("Allocated 1 slot.", createTicketResponse.getResponseMessage());
    }

    @Test
    public void leaveCarFromGarageTest() throws Exception {
        CreateTicketRequest createTicketRequest = new CreateTicketRequest();
        createTicketRequest.setPlate("34-VO-2018");
        createTicketRequest.setColor("Blue");
        createTicketRequest.setVehicleType("Jeep");

        CreateTicketResponse createTicketResponse = ticketService.createTicketForIncomingVehicle(createTicketRequest);
        Long ticketId = createTicketResponse.getTicketId();

        ticketService.leaveCarFromGarage(ticketId);
        assertEquals(0, VehicleSlotsSingleton.getSingletonVehicleSlotInfo().size());
    }

    @Test
    public void garageFullTest() throws Exception {

        CreateTicketRequest createTicketRequest = new CreateTicketRequest();
        createTicketRequest.setPlate("34-SO-1988");
        createTicketRequest.setColor("Black");
        createTicketRequest.setVehicleType("Car");
        ticketService.createTicketForIncomingVehicle(createTicketRequest);

        createTicketRequest = new CreateTicketRequest();
        createTicketRequest.setPlate("34-BO-1987");
        createTicketRequest.setColor("Red");
        createTicketRequest.setVehicleType("Truck");
        ticketService.createTicketForIncomingVehicle(createTicketRequest);

        createTicketRequest = new CreateTicketRequest();
        createTicketRequest.setPlate("34-VO-2018");
        createTicketRequest.setColor("Blue");
        createTicketRequest.setVehicleType("Jeep");
        ticketService.createTicketForIncomingVehicle(createTicketRequest);

        try {
            createTicketRequest = new CreateTicketRequest();
            createTicketRequest.setPlate("34-HBO-2020");
            createTicketRequest.setColor("Black");
            createTicketRequest.setVehicleType("Truck");
            CreateTicketResponse createTicketResponse = ticketService.createTicketForIncomingVehicle(createTicketRequest);
        } catch (NoLeftSlotException noLeftSlotException) {
            assertEquals("Garage is full.", noLeftSlotException.getMessage());
        }

    }

}
