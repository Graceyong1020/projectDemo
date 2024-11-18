package com.projectdemo1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectdemo1.domain.Post;
import com.projectdemo1.domain.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDTO {

    private Long bno;
    private Long cno;

    private String title;
    private String content;
    private String writer;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private Long hitCount;
    private String postType;

    private User user;

    public PostDTO(Post post) {
        this.bno = post.getBno();
        this.cno = post.getCno();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writer = post.getWriter();
        this.createdAt = post.getCreatedAt();

        this.hitCount = post.getHitCount();
        this.postType = post.getPostType();
        this.user = post.getUser();
    }


}
