package com.alexlatkin.filestorage.service.serviceImpl;

import com.alexlatkin.filestorage.exception.tag.TagNotFoundException;
import com.alexlatkin.filestorage.model.entity.Tag;
import com.alexlatkin.filestorage.repository.TagRepository;
import com.alexlatkin.filestorage.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public void addTag(Tag tag) {
        tagRepository.save(tag);
    }

    @Override
    public void delete(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findTagByName(name.toLowerCase()).orElseThrow(() -> new TagNotFoundException("Тэг не найден"));
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public boolean existsTagByTagName(String tagName) {
        return tagRepository.existsTagByName(tagName.toLowerCase());
    }

    @Override
    public void assignFile(Long tagId, Long fileId) {
        tagRepository.assignFile(tagId, fileId);
    }
}
