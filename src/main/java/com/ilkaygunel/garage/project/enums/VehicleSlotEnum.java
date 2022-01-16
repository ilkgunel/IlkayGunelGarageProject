package com.ilkaygunel.garage.project.enums;

public enum VehicleSlotEnum {
    Car(1),
    Jeep(2),
    Truck(4);

    private int slotRange;

    VehicleSlotEnum(int slotRange) {
        this.slotRange = slotRange;
    }

    public int getSlotRange() {
        return this.slotRange;
    }
}
