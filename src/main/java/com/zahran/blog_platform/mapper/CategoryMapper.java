package com.zahran.blog_platform.mapper;

import com.zahran.blog_platform.domain.PostStatus;
import com.zahran.blog_platform.domain.dtos.CategoryDto;
import com.zahran.blog_platform.domain.entity.Category;
import com.zahran.blog_platform.domain.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);

    default long calculatePostCount(List<Post> posts){
        if (posts == null) {
            return 0;
        }
        return posts.stream()
                .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }

}
