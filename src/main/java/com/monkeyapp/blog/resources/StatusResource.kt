/*
MIT License

Copyright (c) 2017 - 2018 Po Cheng

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.monkeyapp.blog.resources

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.monkeyapp.blog.assets.AssetFile
import com.monkeyapp.blog.assets.AssetRepository
import org.glassfish.jersey.server.monitoring.MonitoringStatistics
import javax.inject.Inject
import javax.inject.Provider
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType

@Path("/status")
class StatusResource {
    @Inject
    lateinit var monitoringStatisticsProvider: Provider<MonitoringStatistics>

    @Inject
    lateinit var assetRepository: AssetRepository

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Throws(JsonProcessingException::class)
    fun getStatus(@Context headers: HttpHeaders): String {
        val mapper = ObjectMapper()
        mapper.enable(SerializationFeature.INDENT_OUTPUT)
        val sb = StringBuilder()
        sb.append("headers = ")
        val headerJson = mapper.writeValueAsString(headers.requestHeaders)
        sb.append(headerJson)
        sb.append("\n\n")
        val statistics = monitoringStatisticsProvider!!.get()
        if (statistics != null) {
            sb.append("monitoring = ")
            val monitoringJson = mapper.writeValueAsString(statistics.requestStatistics)
            sb.append(monitoringJson)
            sb.append("\n\n")
        }
        sb.append("posts = ")
        val posts = assetRepository.getPostList()
                .map(AssetFile::metadata)
                .map(AssetFile.Metadata::title)
        sb.append(mapper.writeValueAsString(posts))
        return sb.toString()
    }
}