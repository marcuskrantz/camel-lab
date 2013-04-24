package com.mk.services;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/transfer")
public class ManualTransferService {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response doManualTransfer(@QueryParam("start") final String start
            , @QueryParam("end") final String end
            , @QueryParam("transfertypes") final Set<String> transfertypes) {
        return null;
    }
}
