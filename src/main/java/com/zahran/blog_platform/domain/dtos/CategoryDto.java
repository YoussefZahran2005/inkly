package com.zahran.blog_platform.domain.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CategoryDto {
    private UUID id;
    private String name;
    private long postCount;
}
