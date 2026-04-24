/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.campusapi.store;

/**
 *
 * @author Limuthu Lohiru
 */


import com.campusapi.models.Space;
import com.campusapi.models.Device;
import com.campusapi.models.DeviceReading;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {

    private static final DataStore INSTANCE = new DataStore();

    private final ConcurrentHashMap<String, Space> rooms = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Device> sensors = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<DeviceReading>> readings = new ConcurrentHashMap<>();

    private DataStore() {}

    public static DataStore getInstance() {
        return INSTANCE;
    }

    public ConcurrentHashMap<String, Space> getRooms() { return rooms; }
    public ConcurrentHashMap<String, Device> getSensors() { return sensors; }

    public List<DeviceReading> getReadingsForSensor(String sensorId) {
        readings.putIfAbsent(sensorId, new ArrayList<>());
        return readings.get(sensorId);
    }
}


