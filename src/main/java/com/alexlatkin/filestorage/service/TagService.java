package com.alexlatkin.filestorage.service;

import com.alexlatkin.filestorage.model.entity.Tag;

import java.util.List;

public interface TagService {
    void addTag(Tag tag);
    void delete(Long id);
    Tag getTagByName(String name);
    List<Tag> getAllTags();
    boolean existsTagByTagName(String tagName);
    void assignFile(Long tagId, Long fileId);
}
