package com.monkeyapp.blog.models

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.regex.Pattern
import java.util.stream.Collectors

open class AssetFile(val path: String) {
    open val inputStream: InputStream
        get() = File(path).inputStream()

    val priority: Long
        get() = 0

    val metadata: Metadata by lazy {
        Metadata(path)
    }

    fun readPartialContent(): String {
        return BufferedReader(InputStreamReader(inputStream)).use {
            it.lines().limit(MAX_PARTIAL_FILE_LINES).collect(Collectors.joining(System.lineSeparator()))
        }
    }

    fun readFullContent(): String {
        return BufferedReader(InputStreamReader(inputStream)).use {
            it.lines().collect(Collectors.joining(System.lineSeparator()))
        }
    }

    open val childAssets: List<AssetFile>
        get() {
            return ObjectMapper()
                    .readValue<List<String>>(readFullContent(), object : TypeReference<List<String>>() {})
                    .map(::AssetFile)
        }

    class Metadata(path: String) {
        val title: String
        val tag: String

        val year: String
        val monthday: String
        val time: String

        val createdTime: String
            get() {
                return if (year.isNotEmpty() && monthday.isNotEmpty() && time.isNotEmpty()) {
                    val month = monthday.substring(0, 2)
                    val day = monthday.substring(2)
                    val hour = time.substring(0, 2)
                    val minute = time.substring(2)

                    "$year/$month/$day $hour:$minute"
                } else "unknown"
            }

        init {
            val matcher = NAME_PATTERN.matcher(path)
            if (matcher.matches()) {
                year = matcher.group(1)
                monthday = matcher.group(2)
                time = matcher.group(3)
                tag = matcher.group(4)
                title = matcher.group(5)
            } else {
                year = ""
                monthday = ""
                time = ""
                tag = ""
                title = ""
            }
        }

        companion object {
            private val NAME_PATTERN = Pattern.compile("^(\\d{4})-(\\d{4})-(\\d{4})-(\\w+)-(.+)\\.md$")
        }
    }

    companion object {
        private const val MAX_PARTIAL_FILE_LINES = 32L
    }
}