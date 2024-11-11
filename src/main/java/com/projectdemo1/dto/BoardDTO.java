package com.projectdemo1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projectdemo1.domain.Board;
import com.projectdemo1.domain.Comment;
import com.projectdemo1.domain.User;
import com.projectdemo1.domain.boardContent.BoardImage;
import com.projectdemo1.domain.boardContent.PetType;
import com.projectdemo1.domain.boardContent.PostType;
import com.projectdemo1.domain.boardContent.Status;
import com.projectdemo1.domain.boardContent.color.PetColor;
import com.projectdemo1.domain.boardContent.color.PetColorType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BoardDTO {

    private Long bno;
    private String title;
    private String content;
    private String writer;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<String> fileNames;
    private PostType postType;
    private Long hitCount;
    private Set<BoardImage> imageSet;
    private List<Comment> comments;
    private Status status;
    private String petDescription;
    private Date lostDate;
    private String lostLocation;
    private String petBreeds;
    private String petGender;
    private String petAge;
    private String petWeight;
    private PetType petType;
    private String petName;

    // PetColor 객체로 수정
    private PetColor petColor;
   // private String petColor;

    private User user;
    private String mobile;
    private String email;

    public BoardDTO(Board board) {
        this.bno = board.getBno();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.petColor = board.getPetColor();  // PetColor 객체 그대로 사용
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        this.user = board.getUser();
        this.hitCount = board.getHitCount();
        this.imageSet = board.getImageSet();
        this.comments = board.getComments();
        this.status = board.getStatus();
        this.petDescription = board.getPetDescription();
        this.lostDate = board.getLostDate();
        this.lostLocation = board.getLostLocation();
        this.petBreeds = board.getPetBreeds();
        this.petGender = board.getPetGender();
        this.petAge = board.getPetAge();
        this.petWeight = board.getPetWeight();
        this.petType = board.getPetType();
        this.petName = board.getPetName();
    }
}