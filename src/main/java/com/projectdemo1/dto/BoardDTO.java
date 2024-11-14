package com.projectdemo1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectdemo1.domain.Board;
import com.projectdemo1.domain.Comment;
import com.projectdemo1.domain.User;
import com.projectdemo1.domain.boardContent.BoardImage;
import com.projectdemo1.domain.boardContent.Status;
import com.projectdemo1.domain.boardContent.color.PetColor;
import com.projectdemo1.domain.boardContent.color.PetColorType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<String> fileNames;
    private String postType;
    private Long hitCount;
    private Set<BoardImage> imageSet;
    private List<Comment> comments;
    private Status status;
    private String petDescription;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lostDate;

    private String location;
    private String locationDetail;
    private String petBreeds;
    private String petGender;
    private String petAge;
    private String petWeight;
    private String petType;
    private String petName;
    private Long uno;


    // PetColor 객체로 수정
    private PetColor petColor;
    // private String petColor;
    private PetColorType petColorType;  // Ensure this property exists

    // Getter and Setter methods
    public PetColorType getPetColorType() {
        return petColorType;
    }

    public void setPetColorType(PetColorType petColorType) {
        this.petColorType = petColorType;
    }
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
        this.location = board.getLocation();
        this.locationDetail = board.getLocationDetail();
        this.petBreeds = board.getPetBreeds();
        this.petGender = board.getPetGender();
        this.petAge = board.getPetAge();
        this.petWeight = board.getPetWeight();
        this.petType = board.getPetType();
        this.petName = board.getPetName();
        this.uno =board.getUser().getUno();

    }
}