package com.monkeyapp.blog.dtos;


import com.monkeyapp.blog.models.*;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import java.util.ArrayList;
import java.util.List;

public class TypeConverter implements Converter {
    public TypeConverter() {
        ConvertUtils.register(this, PaperDto.class);
        ConvertUtils.register(this, PaperFileDto.class);
        ConvertUtils.register(this, SyncFeedDto.ChannelDto.class);
        ConvertUtils.register(this, SyncFeedDto.ItemDto.class);
    }

    @SneakyThrows
    public PaperDto toPaperDto(Paper paper) {
        final PaperDto paperDto = new PaperDto();
        BeanUtils.copyProperties(paperDto, paper);
        return paperDto;
    }

    @SneakyThrows
    public PaperChunkDto toPaperChunkDto(PaperChunk paperChunk) {
        final PaperChunkDto paperChunkDto = new PaperChunkDto();
        BeanUtils.copyProperties(paperChunkDto, paperChunk);

        List<PaperDto> paperDtos = new ArrayList<>();
        for(Paper paper : paperChunk.getPapers()) {
            final PaperDto paperDto = new PaperDto();
            BeanUtils.copyProperties(paperDto, paper);
            paperDtos.add(paperDto);
        }

        paperChunkDto.setPapers(paperDtos);
        return paperChunkDto;
    }

    @SneakyThrows
    public List<TagCounterDto> tagCounterDto(List<TagCounter> tagCounters) {
        final List<TagCounterDto> tagCounterDtos = new ArrayList<>(tagCounters.size());
        for(TagCounter tagCounter : tagCounters) {
            final TagCounterDto tagCounterDto = new TagCounterDto();
            BeanUtils.copyProperties(tagCounterDto, tagCounter);
            tagCounterDtos.add(tagCounterDto);
        }

        return tagCounterDtos;
    }

    @SneakyThrows
    public SyncFeedDto toSyncFeedDto(SyncFeed syncFeed) {
        final SyncFeedDto syncFeedDto = new SyncFeedDto();
        BeanUtils.copyProperties(syncFeedDto, syncFeed);

        final ArrayList<SyncFeedDto.ItemDto> itemDtos = new ArrayList<>();
        for(SyncFeed.Item item : syncFeed.getChannel().getItems()) {
            final SyncFeedDto.ItemDto itemDto = new SyncFeedDto.ItemDto();
            BeanUtils.copyProperties(itemDto, item);
            itemDtos.add(itemDto);
        }

        syncFeedDto.channel.setItems(itemDtos);
        return syncFeedDto;
    }

    @Override
    @SneakyThrows
    public <T> T convert(Class<T> type, Object value) {
        if (value instanceof Paper) {
            final PaperDto paperDto = new PaperDto();
            BeanUtils.copyProperties(paperDto, value);
            return type.cast(paperDto);
        }

        if (value instanceof PaperFile) {
            final PaperFileDto fileDto = new PaperFileDto();
            BeanUtils.copyProperties(fileDto, value);
            return type.cast(fileDto);
        }

        if (value instanceof SyncFeed.Channel) {
            final SyncFeedDto.ChannelDto channelDto = new SyncFeedDto.ChannelDto();
            BeanUtils.copyProperties(channelDto, value);
            return type.cast(channelDto);
        }

        if (value instanceof SyncFeed.Item) {
            final SyncFeedDto.ItemDto itemDto = new SyncFeedDto.ItemDto();
            BeanUtils.copyProperties(itemDto, value);
            return type.cast(itemDto);
        }

        return null;
    }
}
