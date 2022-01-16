package com.ilkaygunel.garage.project.model;

import java.util.List;

public class VehicleInfo {
    private String plate;
    private String color;
    private String vehicleType;
    List<Integer> allocatedSlots;

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public List<Integer> getAllocatedSlots() {
        return allocatedSlots;
    }

    public void setAllocatedSlots(List<Integer> allocatedSlots) {
        this.allocatedSlots = allocatedSlots;
    }
}
