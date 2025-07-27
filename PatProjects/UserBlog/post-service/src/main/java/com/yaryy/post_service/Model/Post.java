package com.yaryy.post_service.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String content;
    private LocalDateTime articleDate;

    @PrePersist
    protected void onCreate() {
        this.articleDate = LocalDateTime.now();
    }
}
