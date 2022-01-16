package com.ilkaygunel.garage.project.singleton;

import com.ilkaygunel.garage.project.model.VehicleInfo;

import java.util.concurrent.ConcurrentHashMap;

public class VehicleSlotsSingleton {
    private VehicleSlotsSingleton() {

    }

    private static ConcurrentHashMap<Long, VehicleInfo> vehicleSlotInfo;

    public static ConcurrentHashMap<Long, VehicleInfo> getSingletonVehicleSlotInfo() {
        synchronized (SlotsSingleton.class) {
            if (vehicleSlotInfo == null) {
                vehicleSlotInfo = new ConcurrentHashMap();
            }
        }
        return vehicleSlotInfo;
    }
}
