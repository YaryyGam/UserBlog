package com.yaryy.comment_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class CommentResponse {
    private long id;
    private String content;
    private LocalDate date;
}
