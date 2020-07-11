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
package com.monkeyapp.blog.dtos


import com.monkeyapp.blog.models.*
import org.apache.commons.beanutils.BeanUtils
import org.apache.commons.beanutils.ConvertUtils
import org.apache.commons.beanutils.Converter
import java.util.*

class TypeConverter : Converter {

    fun toPaperDto(paper: Paper?): PaperDto {
        val paperDto = PaperDto()
        BeanUtils.copyProperties(paperDto, paper)
        return paperDto
    }

    fun toPaperChunkDto(paperChunk: PaperChunk): PaperChunkDto {
        val paperChunkDto = PaperChunkDto()
        BeanUtils.copyProperties(paperChunkDto, paperChunk)
        val paperDtos: MutableList<PaperDto> = ArrayList()
        for (paper in paperChunk.papers) {
            val paperDto = PaperDto()
            BeanUtils.copyProperties(paperDto, paper)
            paperDtos.add(paperDto)
        }
        paperChunkDto.papers = paperDtos
        return paperChunkDto
    }

    fun tagCounterDto(tagCounters: List<TagCounter?>): List<TagCounterDto> {
        val tagCounterDtos: MutableList<TagCounterDto> = ArrayList(tagCounters.size)
        for (tagCounter in tagCounters) {
            val tagCounterDto = TagCounterDto()
            BeanUtils.copyProperties(tagCounterDto, tagCounter)
            tagCounterDtos.add(tagCounterDto)
        }
        return tagCounterDtos
    }

    fun toSyncFeedDto(syncFeed: SyncFeed): SyncFeedDto {
        val syncFeedDto = SyncFeedDto()
        BeanUtils.copyProperties(syncFeedDto, syncFeed)
        val itemDtos = ArrayList<ItemDto>()
        for (item in syncFeed.channel.items) {
            val itemDto = ItemDto()
            BeanUtils.copyProperties(itemDto, item)
            itemDtos.add(itemDto)
        }
        syncFeedDto.channel?.items = itemDtos
        return syncFeedDto
    }

    override fun <T> convert(type: Class<T>, value: Any): T? {
        if (value is Paper) {
            val paperDto = PaperDto()
            BeanUtils.copyProperties(paperDto, value)
            return type.cast(paperDto)
        }
        if (value is PaperFile) {
            val fileDto = PaperFileDto()
            BeanUtils.copyProperties(fileDto, value)
            return type.cast(fileDto)
        }
        if (value is Channel) {
            val channelDto = ChannelDto()
            BeanUtils.copyProperties(channelDto, value)
            return type.cast(channelDto)
        }
        if (value is Item) {
            val itemDto = ItemDto()
            BeanUtils.copyProperties(itemDto, value)
            return type.cast(itemDto)
        }
        return null
    }

    init {
        ConvertUtils.register(this, PaperDto::class.java)
        ConvertUtils.register(this, PaperFileDto::class.java)
        ConvertUtils.register(this, ChannelDto::class.java)
        ConvertUtils.register(this, ItemDto::class.java)
    }
}