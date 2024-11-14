package com.projectdemo1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardListReplyCountDTO {

    private Long bno;
    private String title;
    private String writer;
    private LocalDateTime createdAt;

    private Long replyCount;
}
