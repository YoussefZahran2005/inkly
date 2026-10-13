package com.zahran.blog_platform.service;

import com.zahran.blog_platform.domain.entity.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
}
