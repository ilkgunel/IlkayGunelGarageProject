package com.ilkaygunel.garage.project.singleton;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class SlotsSingleton {

    private SlotsSingleton() {

    }

    private static ConcurrentHashMap<Integer, String> slots;

    public static ConcurrentHashMap<Integer, String> getSingletonSlots(int initialSlotRange) {
        synchronized (SlotsSingleton.class) {
            if (slots == null) {
                slots = new ConcurrentHashMap();
                for (int i = 1; i <= initialSlotRange; i++) {
                    slots.put(i, "available");
                }
            }
        }
        return slots;
    }
}