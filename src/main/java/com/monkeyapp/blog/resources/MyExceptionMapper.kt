package com.monkeyapp.blog.resources

import org.apache.log4j.Logger
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper

class MyExceptionMapper : ExceptionMapper<Exception> {
    var logger = Logger.getLogger(MyExceptionMapper::class.java)
    override fun toResponse(exception: Exception): Response {
        logger.debug("Exception received: ", exception)
        return if (exception is WebApplicationException) {
            exception.response
        } else Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(exception.cause)
                .build()
    }
}