package com.zahran.blog_platform.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
    private int status;
    private String message;
    private List<FieldError> errors;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String message;
    }

    /* EXAMPLE
    {
  "status": 400,
  "message": "Validation Failed",
  "errors": [
    {
      "field": "title",
      "message": "Title cannot be blank"
    },
    {
      "field": "content",
      "message": "Content must be at least 10 characters"
    }
  ]
}

*/
}