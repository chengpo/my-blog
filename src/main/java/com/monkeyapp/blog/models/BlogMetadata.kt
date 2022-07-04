package com.monkeyapp.blog.models

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