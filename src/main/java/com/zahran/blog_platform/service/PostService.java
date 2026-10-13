package com.zahran.blog_platform.service;

import com.zahran.blog_platform.domain.CreatePostRequest;
import com.zahran.blog_platform.domain.UpdatePostRequest;
import com.zahran.blog_platform.domain.dtos.CreatePostRequestDto;
import com.zahran.blog_platform.domain.entity.Post;
import com.zahran.blog_platform.domain.entity.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> getAllPosts(UUID categoryId, UUID tagId);

    List<Post> getDraftPosts(User loggedInUser);

    Post createPost(User user, CreatePostRequest createPostRequest);

    Post updatePost(UUID id, UpdatePostRequest updatePostRequest);

    Post getPost(UUID id);

    void deletePost(UUID id);
}
