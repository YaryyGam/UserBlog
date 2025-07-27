package com.yaryy.post_service.Model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PostRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
}
