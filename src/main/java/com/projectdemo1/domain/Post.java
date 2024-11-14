package com.projectdemo1.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private LocalDateTime createdAt;

    @Builder.Default
    private Long views = 0L; // 조회수 필드, 기본값 0

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    private Long bno;
    private Long cno;

    private int hitCount;
    private String postType;

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getViews() {
        return views;
    }

    public User getUser() {
        return user;
    }

    public Long getBno() {
        return bno;
    }

    public int getHitCount() {
        return hitCount;
    }

    public Long getCno() {
        return cno;
    }

    public String getPostType() {
        return postType;
    }

    public String getWriter() {
        return user.getUsername(); // Assuming User class has getUsername method
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setBno(Long bno) {
        this.bno = bno;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    public void setCno(Long cno) {
        this.cno = cno;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

}