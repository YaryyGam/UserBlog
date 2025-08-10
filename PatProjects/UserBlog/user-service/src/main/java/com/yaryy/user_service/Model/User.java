package com.yaryy.user_service.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userName;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private Role role;

    @ElementCollection
    private List<Integer> postIds;

    @ElementCollection
    private List<Long> commentIds;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.role = Role.USER;
    }
}
