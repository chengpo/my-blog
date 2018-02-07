package com.monkeyapp.blog.resources;

import org.apache.log4j.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class MyExceptionMapper implements ExceptionMapper<Exception> {
    Logger logger = Logger.getLogger(MyExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {
        logger.debug("Exception received: ", exception);

        if (exception instanceof WebApplicationException) {
            return ((WebApplicationException) exception).getResponse();
        }

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(exception.getCause())
                .build();
    }
}
