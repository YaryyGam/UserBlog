package com.yaryy.post_service.Model;

import java.time.LocalDateTime;

public class PostResponse {
    private int id;
    private String title;
    private String content;
    private LocalDateTime articleDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getArticleDate() {
        return articleDate;
    }

    public void setArticleDate(LocalDateTime articleDate) {
        this.articleDate = articleDate;
    }
}
