package com.alexlatkin.filestorage.service.serviceImpl;

import com.alexlatkin.filestorage.model.entity.Tag;
import com.alexlatkin.filestorage.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @InjectMocks
    TagServiceImpl tagService;
    @Mock
    TagRepository tagRepository;

    @Test
    void addTag() {
        Tag tagMock = mock(Tag.class);

        tagService.addTag(tagMock);

        verify(tagRepository, times(1)).save(tagMock);
    }

    @Test
    void delete() {

        tagService.delete(123456789L);

        verify(tagRepository, times(1)).deleteById(123456789L);
    }

    @Test
    void getTagByName() {
        String tagName = "tagname";
        Tag expectedTag = new Tag(1L, tagName);

        when(tagRepository.findTagByName(tagName)).thenReturn(Optional.of(expectedTag));

        var result = tagService.getTagByName(tagName);

        assertEquals(expectedTag, result);
        verify(tagRepository, times(1)).findTagByName(tagName);
    }

    @Test
    void getAllTags() {
        List<Tag> expectedList = List.of(new Tag(1L, "firstTag"), new Tag(2L, "secondTag"));

        when(tagRepository.findAll()).thenReturn(expectedList);

        var result = tagService.getAllTags();

        assertEquals(expectedList, result);
        verify(tagRepository, times(1)).findAll();
    }

    @Test
    void existsTagByTagName() {
        String tagName = "tagname";

        when(tagRepository.existsTagByName(tagName)).thenReturn(true);

        var result = tagService.existsTagByTagName(tagName);

        assertTrue(result);
        verify(tagRepository, times(1)).existsTagByName(tagName);
    }

    @Test
    void assignFile() {
        tagRepository.assignFile(1L, 1L);

        verify(tagRepository, times(1)).assignFile(1L, 1L);
    }
}