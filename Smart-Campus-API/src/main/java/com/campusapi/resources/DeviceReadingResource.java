/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.campusapi.resources;

/**
 *
 * @author Limuthu Lohiru
 */



import com.campusapi.exceptions.DeviceUnavailableException;
import com.campusapi.models.Device;
import com.campusapi.models.DeviceReading;
import com.campusapi.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceReadingResource {

    private final String sensorId;
    private final DataStore store = DataStore.getInstance();

    public DeviceReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getReadings() {
        List<DeviceReading> readings = store.getReadingsForSensor(sensorId);
        return Response.ok(readings).build();
    }

    @POST
    public Response addReading(DeviceReading reading) {
        Device Device = store.getSensors().get(sensorId);

        if (Device == null) {
            return Response.status(404)
                .entity(errorBody(404, "Not Found", "Device " + sensorId + " not found"))
                .build();
        }

        // Block if Device is in MAINTENANCE
        if ("MAINTENANCE".equalsIgnoreCase(Device.getStatus())) {
            throw new DeviceUnavailableException(
                "Device " + sensorId + " is under MAINTENANCE and cannot accept new readings."
            );
        }

        // Create proper reading with UUID and timestamp
        DeviceReading newReading = new DeviceReading(reading.getValue());

        // Add to readings list
        store.getReadingsForSensor(sensorId).add(newReading);

        // Update Device's currentValue
        Device.setCurrentValue(newReading.getValue());

        return Response.status(201).entity(newReading).build();
    }

    private Map<String, Object> errorBody(int status, String error, String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("error", error);
        map.put("message", message);
        return map;
    }
}


