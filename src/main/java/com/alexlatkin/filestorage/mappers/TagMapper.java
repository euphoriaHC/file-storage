package com.alexlatkin.filestorage.mappers;

import com.alexlatkin.filestorage.dto.tag.TagDto;
import com.alexlatkin.filestorage.model.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagMapper {

    public Tag toEntity(TagDto tagDto) {
        return new Tag(tagDto.getId(), tagDto.getName());
    }

    public TagDto toDto(Tag tag) {
        return new TagDto(tag.getId(), tag.getName());
    }

    public List<TagDto> toListDto(List<Tag> tagList) {
        return tagList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
