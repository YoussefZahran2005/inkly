package com.zahran.blog_platform.repository;


import com.zahran.blog_platform.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query("SELECT c FROM c LEFT JOIN FETCH c.posts")
    List<Category> findAllWithPostCount();
}