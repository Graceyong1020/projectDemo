package com.projectdemo1.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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
    private Long uno;

    private Long bno;
    private Long cno;


    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String writer;

    private LocalDateTime createdAt;

    @ColumnDefault("0") //기본값 0
    private Long hitCount; //조회수

    private String postType;


    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }





}
