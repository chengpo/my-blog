package com.monkeyapp.blog.dtos

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "urlset")
data class SitemapDto(
    @JvmField
    @field:XmlAttribute(name = "xmlns")
    val xmlns: String = "http://www.sitemaps.org/schemas/sitemap/0.9",

    @JvmField
    @field:XmlElement(name="url")
    val url: List<SiteUrlDto> = emptyList()
)

data class SiteUrlDto(
    @JvmField
    @field:XmlElement(name="loc")
    val loc: String
)
