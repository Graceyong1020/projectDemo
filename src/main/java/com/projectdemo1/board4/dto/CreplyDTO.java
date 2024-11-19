package com.projectdemo1.board4.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class CreplyDTO {

    private Long rno;


    private Long cno;


    @NotEmpty
    private String replyText;
    @NotEmpty
    private String replyer;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime updatedAt;

    private Long parentRno;
}
