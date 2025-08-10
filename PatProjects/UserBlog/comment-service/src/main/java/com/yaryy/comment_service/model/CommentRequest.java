package com.yaryy.comment_service.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentRequest {
    @NotBlank
    private String content;
}
