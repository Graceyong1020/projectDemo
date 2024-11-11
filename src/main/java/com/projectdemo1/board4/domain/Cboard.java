package com.projectdemo1.board4.domain;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.projectdemo1.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "imageSet")
public class Cboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;


    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private String writer;

   /* @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uno", nullable = false)
    private User user;
*/
    //image
    @OneToMany(mappedBy = "cboard", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 20)
    private Set<CboardImage> imageSet = new HashSet<>();
    //image
    public void addImage(String uuid, String fileName) {
        CboardImage image = CboardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .cboard(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(image);
    }
    public void clearImages() {
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));
        this.imageSet.clear();
    }

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdAt")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updatedAt")
    private LocalDateTime updatedAt;


//빌드 오류 나서 수정
//    @ColumnDefault("0")
//    private Long hitCount = 0L;
//    @ColumnDefault("0")
//    private Long replyCount = 0L;


    @Builder.Default
    private Long hitCount = 0L;

    @Builder.Default
    private Long replyCount = 0L;

    public void incrementReplyCount() {
        this.replyCount++;
    }

    public void decrementReplyCount() {
        this.replyCount--;
    }

    @PrePersist
    public void prePersist() {
        this.hitCount = this.hitCount == null ? 0 : this.hitCount;
        this.replyCount = this.replyCount == null ? 0 : this.replyCount;
    }



    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }



}
