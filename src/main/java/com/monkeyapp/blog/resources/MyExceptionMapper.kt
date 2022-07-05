package com.monkeyapp.blog.resources



import org.apache.logging.log4j.LogManager
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

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