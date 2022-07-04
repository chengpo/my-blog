package com.monkeyapp.blog.models

import java.util.regex.Pattern

class BlogMetadataFactory() {
    fun create(path: String, name: String): BlogMetadata? {
        val matcher = NAME_PATTERN.matcher(name)
        return if (matcher.matches()) {
            val year=matcher.group(1)

            val monthday=matcher.group(2)
            val month = monthday.substring(0, 2)
            val day = monthday.substring(2)

            val time=matcher.group(3)
            val hour = time.substring(0, 2)
            val minute = time.substring(2)

            BlogMetadata(
                tag = matcher.group(4),
                title = matcher.group(5),
                year = year,
                monthday= monthday,
                time = time,
                crtime = "$year/$month/$day $hour:$minute",
                path = path
            )
        } else { null }
    }

    companion object {
        val NAME_PATTERN: Pattern = Pattern.compile("^(\\d{4})-(\\d{4})-(\\d{4})-(\\w+)-(.+)\\.md$")
    }
}