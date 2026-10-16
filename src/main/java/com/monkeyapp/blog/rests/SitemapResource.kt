package com.monkeyapp.blog.rests

import com.monkeyapp.blog.di.RootScope
import com.monkeyapp.blog.dtos.SitemapDto
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("/sitemap")
class SitemapResource {
    @Inject
    private lateinit var rootScope: RootScope
    
    @GET
    @Produces("application/xml")
    fun sitemap(): SitemapDto = rootScope.visitorScope().sitemapController().sitemap()
}
