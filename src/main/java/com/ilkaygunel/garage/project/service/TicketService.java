package com.ilkaygunel.garage.project.service;

import com.ilkaygunel.garage.project.exceptionhandling.AlreadyAllocatedException;
import com.ilkaygunel.garage.project.model.CreateTicketRequest;
import com.ilkaygunel.garage.project.enums.VehicleSlotEnum;
import com.ilkaygunel.garage.project.exceptionhandling.NoLeftSlotException;
import com.ilkaygunel.garage.project.exceptionhandling.NotEnoughSlotException;
import com.ilkaygunel.garage.project.model.CreateTicketResponse;
import com.ilkaygunel.garage.project.model.StatusResponse;
import com.ilkaygunel.garage.project.model.VehicleInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class TicketService {

    Logger logger = Logger.getLogger(TicketService.class.getName());

    @Value("${initialSlotRange}")
    private Integer initialSlotRange;

    private ConcurrentHashMap<Long, VehicleInfo> vehicleSlotInfo = new ConcurrentHashMap<>(); //Put ticketId and allocated slots
    private ConcurrentHashMap<Integer, String> slots = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        for (int i = 1; i <= initialSlotRange; i++) {
            slots.put(i, "available");
        }
    }

    public CreateTicketResponse createTicketForIncomingVehicle(CreateTicketRequest createTicketRequest) throws Exception {
        if (vehicleSlotInfo.entrySet().stream().anyMatch(entry -> entry.getValue().getPlate().equalsIgnoreCase(createTicketRequest.getPlate()))) {
            throw new AlreadyAllocatedException("This plate already has allocated slots!");
        }

        List<Integer> availableSlotNumbers = new ArrayList<>(slots.entrySet().stream().filter(entry -> entry.getValue().equals("available"))
                .collect(Collectors.toConcurrentMap(x -> x.getKey(), x -> x.getValue())).keySet()).stream().sorted().collect(Collectors.toList());
        int slotSizeOfVehicle = VehicleSlotEnum.valueOf(createTicketRequest.getVehicleType()).getSlotRange();

        if (availableSlotNumbers.size() == 0) {
            throw new NoLeftSlotException("Garage is full.");
        } else if (slotSizeOfVehicle > availableSlotNumbers.size()) {
            throw new NotEnoughSlotException("There is not enough slot!");
        }

        Random random = new Random();
        CreateTicketResponse createTicketResponse = new CreateTicketResponse();
        createTicketResponse.setTicketId(random.nextLong());

        logger.info("Allocating Unit Range ->" + slotSizeOfVehicle);

        //Mark as allocated slot(s) for that vehicle
        List<Integer> allocatedSlotsForThatVehicle = new ArrayList<>();
        for (int i = 0; i < slotSizeOfVehicle; i++) {
            slots.put(availableSlotNumbers.get(i), "allocated");
            allocatedSlotsForThatVehicle.add(availableSlotNumbers.get(i));
        }

        VehicleInfo vehicleInfo = prepareVehicleInfo(createTicketRequest, allocatedSlotsForThatVehicle);
        int slotForMarkingAsPreAllocated = (allocatedSlotsForThatVehicle.get(allocatedSlotsForThatVehicle.size() - 1)) + 1;
        logger.info("Marking slot " + slotForMarkingAsPreAllocated + " as preAllocated");
        slots.put(slotForMarkingAsPreAllocated, "preAllocated");
        //createTicketResponse.setVehicleInfo(vehicleInfo);

        createTicketResponse.setResponseMessage("Allocated " + slotSizeOfVehicle + " slot.");
        vehicleSlotInfo.put(createTicketResponse.getTicketId(), vehicleInfo);

        return createTicketResponse;
    }

    public void leaveCarFromGarage(Long ticketId) {
        VehicleInfo vehicleInfo = vehicleSlotInfo.entrySet().stream().filter(vehicleSlotInfoEntry -> vehicleSlotInfoEntry.getKey().equals(ticketId)).findFirst().get().getValue();
        List<Integer> allocatedSlots = vehicleInfo.getAllocatedSlots();
        for (Integer slot : allocatedSlots) {
            slots.put(slot, "available");
        }
        vehicleSlotInfo.entrySet().removeIf(vehicleSlotInfoEntry -> vehicleSlotInfoEntry.getKey().equals(ticketId));
    }

    public List<StatusResponse> prepareAndGetStatusReport() {
        List<StatusResponse> statusResponseList = new ArrayList<>();
        vehicleSlotInfo.entrySet().stream().forEach(vehicleSlotInfoEntry -> {
            VehicleInfo vehicleInfo = vehicleSlotInfoEntry.getValue();

            StatusResponse statusResponse = new StatusResponse();
            statusResponse.setPlate(vehicleInfo.getPlate());
            statusResponse.setAllocatedSlots(vehicleInfo.getAllocatedSlots());
            statusResponseList.add(statusResponse);
        });
        return statusResponseList;
    }

    private VehicleInfo prepareVehicleInfo(CreateTicketRequest createTicketRequest, List<Integer> allocatedSlots) {
        VehicleInfo vehicleInfo = new VehicleInfo();
        vehicleInfo.setPlate(createTicketRequest.getPlate());
        vehicleInfo.setColor(createTicketRequest.getColor());
        vehicleInfo.setVehicleType(createTicketRequest.getVehicleType());
        vehicleInfo.setAllocatedSlots(allocatedSlots);
        return vehicleInfo;
    }
}
