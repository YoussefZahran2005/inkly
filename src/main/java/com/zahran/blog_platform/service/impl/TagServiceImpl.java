package com.zahran.blog_platform.service.impl;

import com.zahran.blog_platform.domain.entity.Tag;
import com.zahran.blog_platform.repository.TagRepository;
import com.zahran.blog_platform.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> getTags() {
        return tagRepository.findAllWithPostCount();
    }

}
