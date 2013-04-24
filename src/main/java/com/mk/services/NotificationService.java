package com.mk.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Map;

@Path("/notification")
public class NotificationService {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response processNotification(Map<String, Object> body) {
        return null;
    }

}
