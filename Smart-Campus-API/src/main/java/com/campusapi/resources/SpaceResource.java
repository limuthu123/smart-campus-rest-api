/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.campusapi.resources;

/**
 *
 * @author Limuthu Lohiru
 */


import com.campusapi.exceptions.SpaceNotEmptyException;
import com.campusapi.models.Space;
import com.campusapi.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SpaceResource {

    private final DataStore store = DataStore.getInstance();

    @GET
    public Response getAllRooms() {
        List<Space> roomList = new ArrayList<>(store.getRooms().values());
        return Response.ok(roomList).build();
    }

    @POST
    public Response createRoom(Space Space) {
        if (Space.getId() == null || Space.getId().isEmpty()) {
            return Response.status(400)
                .entity(errorBody(400, "Bad Request", "Space ID is required"))
                .build();
        }
        if (store.getRooms().containsKey(Space.getId())) {
            return Response.status(409)
                .entity(errorBody(409, "Conflict", "Space with this ID already exists"))
                .build();
        }
        store.getRooms().put(Space.getId(), Space);
        return Response.status(201).entity(Space).build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Space Space = store.getRooms().get(roomId);
        if (Space == null) {
            return Response.status(404)
                .entity(errorBody(404, "Not Found", "Space " + roomId + " not found"))
                .build();
        }
        return Response.ok(Space).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Space Space = store.getRooms().get(roomId);
        if (Space == null) {
            return Response.status(404)
                .entity(errorBody(404, "Not Found", "Space " + roomId + " not found"))
                .build();
        }
        if (!Space.getSensorIds().isEmpty()) {
            throw new SpaceNotEmptyException("Space " + roomId + " still has sensors assigned. Remove sensors first.");
        }
        store.getRooms().remove(roomId);
        return Response.noContent().build();
    }

    private java.util.Map<String, Object> errorBody(int status, String error, String message) {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("status", status);
        map.put("error", error);
        map.put("message", message);
        return map;
    }
}


