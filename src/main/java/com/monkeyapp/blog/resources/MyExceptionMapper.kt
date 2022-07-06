package com.monkeyapp.blog.resources

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class MyExceptionMapper : ExceptionMapper<Exception> {
    private val logger: Logger = LogManager.getLogger(MyExceptionMapper::class.java)
    override fun toResponse(exception: Exception): Response {
        logger.debug("Exception received: ${exception.message}")
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity("Exception received: ${exception.message}")
                .build()
    }
}