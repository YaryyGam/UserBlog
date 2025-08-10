package com.yaryy.user_service.Model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Comment {
    private long id;
    private String content;
}
