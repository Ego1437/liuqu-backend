package com.omate.liuqu.service;


import com.omate.liuqu.model.Tag;
import com.omate.liuqu.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }
    public Iterable<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Optional<Tag> getTagById(Long id) {
        return tagRepository.findById(id);
    }

    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    public Tag updateTag(Long id, Tag updatedTag) {
        return tagRepository.findById(id)
                .map(tag -> {
                    tag.setTagName(updatedTag.getTagName());
                    return tagRepository.save(tag);
                }).orElseGet(() -> {
                    updatedTag.setTagId(id);
                    return tagRepository.save(updatedTag);
                });
    }
}

