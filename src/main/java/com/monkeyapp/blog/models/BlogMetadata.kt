package com.monkeyapp.blog.models

import java.text.SimpleDateFormat

data class BlogMetadata(
    val title: String,
    val tag: String,
    val year: String,
    val monthday: String,
    val time: String,
    val crtime: String,
    val path: String)

fun BlogMetadata.priority(): Long {
        return year.toLong() * 10000L * 10000L +
            monthday.toLong() * 10000L +
            time.toLong()
}

val BlogMetadata.capitalizedTitle: String
    get() {
        return title.split("-").joinToString(" ") { w -> w.replaceFirstChar { it.uppercase() } }
    }

val BlogMetadata.capitalizedTag: String
    get() {
        return tag.replaceFirstChar { it.uppercase() }
    }

val BlogMetadata.postUrl: String
    get() {
        return "posts/${year}/${monthday}/${title}"
    }

val BlogMetadata.pubDate: String
    get() {
        val date = SimpleDateFormat("yyyy/MM/dd hh:mm").parse(crtime)
        return SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z").format(date)
    }