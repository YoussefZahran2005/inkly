package com.zahran.blog_platform.repository;


import com.zahran.blog_platform.domain.PostStatus;
import com.zahran.blog_platform.domain.entity.Category;
import com.zahran.blog_platform.domain.entity.Post;
import com.zahran.blog_platform.domain.entity.Tag;
import com.zahran.blog_platform.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByStatusAndCategoryAndTagsContaining(PostStatus status, Category category, Tag tag);
    List<Post> findAllByStatusAndCategory(PostStatus status, Category category);
    List<Post> findAllByStatusAndTagsContaining(PostStatus status, Tag tag);
    List<Post> findAllByStatus(PostStatus status);
    List<Post> findAllByAuthorAndStatus(User author, PostStatus status);
}
