/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.campusapi.exceptions;

/**
 *
 * @author Limuthu Lohiru
 */



import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Provider
public class DeviceUnavailableExceptionMapper implements ExceptionMapper<DeviceUnavailableException> {

    @Override
    public Response toResponse(DeviceUnavailableException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", 403);
        error.put("error", "Forbidden");
        error.put("message", ex.getMessage());
        return Response.status(403)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}


