package com.zahran.blog_platform.service;

import com.zahran.blog_platform.domain.entity.Tag;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TagService {
    List<Tag> getTags();
}
