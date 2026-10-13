package com.zahran.blog_platform.mapper;

import com.zahran.blog_platform.domain.CreatePostRequest;
import com.zahran.blog_platform.domain.dtos.CreatePostRequestDto;
import com.zahran.blog_platform.domain.dtos.PostDto;
import com.zahran.blog_platform.domain.entity.Post;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "status", source = "status")
    PostDto toDto(Post post);


    CreatePostRequest toCreatePostRequest(@Valid CreatePostRequestDto createPostRequestDto);

}
