package com.yaryy.post_service.Model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {
    private int id;
    private String title;
    private String content;
    private LocalDateTime articleDate;
    private String city;
    private String temperature;
}
