package com.monkeyapp.blog.rests

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
        logger.info("Exception received: ${exception.cause}")
        return Response
                .status(Response.Status.NOT_FOUND)
                .type(MediaType.TEXT_PLAIN)
                .entity(errorDetails(exception))
                .build()
    }
    
    private fun errorDetails(exception: Exception): String = """
Message: 
    ${exception.message}

Cause:
    ${exception.cause}
"""
}
