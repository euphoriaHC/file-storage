package com.alexlatkin.filestorage.controller;

import com.alexlatkin.filestorage.dto.tag.TagDto;
import com.alexlatkin.filestorage.mappers.TagMapper;
import com.alexlatkin.filestorage.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping("/all")
    public List<TagDto> getTags() {
        return tagMapper.toListDto(tagService.getAllTags());
    }
}
