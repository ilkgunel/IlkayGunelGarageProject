package com.ilkaygunel.garage.project.model;

import java.util.List;

public class StatusResponse {
    private String plate;
    private List<Integer> allocatedSlots;

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public List<Integer> getAllocatedSlots() {
        return allocatedSlots;
    }

    public void setAllocatedSlots(List<Integer> allocatedSlots) {
        this.allocatedSlots = allocatedSlots;
    }
}
