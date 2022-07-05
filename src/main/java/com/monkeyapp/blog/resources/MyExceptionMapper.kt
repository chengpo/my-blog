package com.monkeyapp.blog.resources



import org.apache.logging.log4j.LogManager
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider

@Provider
class MyExceptionMapper : ExceptionMapper<Exception> {
    var logger = LogManager.getLogger(MyExceptionMapper::class.java)
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