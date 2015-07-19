
package com.nscube.services.leaguemanager;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider public class IllegalArgumentMapper
          implements ExceptionMapper<IllegalArgumentException> {

    public Response toResponse(IllegalArgumentException ex) {

        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(ex.getMessage())
                       .type(MediaType.TEXT_PLAIN_TYPE)
                       .build();

    }

}
