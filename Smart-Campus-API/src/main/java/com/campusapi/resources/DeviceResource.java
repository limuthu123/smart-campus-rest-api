/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.campusapi.resources;

/**
 *
 * @author Limuthu Lohiru
 */


import com.campusapi.exceptions.LinkedResourceNotFoundException;
import com.campusapi.models.Device;
import com.campusapi.models.Space;
import com.campusapi.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceResource {

    private final DataStore store = DataStore.getInstance();

    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Device> sensorList = new ArrayList<>(store.getSensors().values());

        if (type != null && !type.isEmpty()) {
            List<Device> filtered = new ArrayList<>();
            for (Device s : sensorList) {
                if (s.getType().equalsIgnoreCase(type)) {
                    filtered.add(s);
                }
            }
            return Response.ok(filtered).build();
        }

        return Response.ok(sensorList).build();
    }

    @POST
    public Response createSensor(Device Device) {
        if (Device.getId() == null || Device.getId().isEmpty()) {
            return Response.status(400)
                .entity(errorBody(400, "Bad Request", "Device ID is required"))
                .build();
        }

        if (store.getSensors().containsKey(Device.getId())) {
            return Response.status(409)
                .entity(errorBody(409, "Conflict", "Device with this ID already exists"))
                .build();
        }

        // Validate that the roomId exists
        if (Device.getRoomId() == null || !store.getRooms().containsKey(Device.getRoomId())) {
            throw new LinkedResourceNotFoundException(
                "Space with ID '" + Device.getRoomId() + "' does not exist. Cannot register Device."
            );
        }

        // Add Device to the Space's sensorIds list
        Space Space = store.getRooms().get(Device.getRoomId());
        Space.getSensorIds().add(Device.getId());

        // Default status if not provided
        if (Device.getStatus() == null || Device.getStatus().isEmpty()) {
            Device.setStatus("ACTIVE");
        }

        store.getSensors().put(Device.getId(), Device);
        return Response.status(201).entity(Device).build();
    }

    @GET
    @Path("/{sensorId}")
    public Response getSensor(@PathParam("sensorId") String sensorId) {
        Device Device = store.getSensors().get(sensorId);
        if (Device == null) {
            return Response.status(404)
                .entity(errorBody(404, "Not Found", "Device " + sensorId + " not found"))
                .build();
        }
        return Response.ok(Device).build();
    }

    @DELETE
    @Path("/{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        Device Device = store.getSensors().get(sensorId);
        if (Device == null) {
            return Response.status(404)
                .entity(errorBody(404, "Not Found", "Device " + sensorId + " not found"))
                .build();
        }

        // Remove Device from Space's sensorIds list
        Space Space = store.getRooms().get(Device.getRoomId());
        if (Space != null) {
            Space.getSensorIds().remove(sensorId);
        }

        store.getSensors().remove(sensorId);
        return Response.noContent().build();
    }

    @Path("/{sensorId}/readings")
    public DeviceReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        Device Device = store.getSensors().get(sensorId);
        if (Device == null) {
            throw new NotFoundException("Device " + sensorId + " not found");
        }
        return new DeviceReadingResource(sensorId);
    }

    private Map<String, Object> errorBody(int status, String error, String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("error", error);
        map.put("message", message);
        return map;
    }
}


