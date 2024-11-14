package com.projectdemo1.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projectdemo1.domain.boardContent.BoardImage;
import com.projectdemo1.domain.boardContent.Status;
import com.projectdemo1.domain.boardContent.color.PetColor;
import com.projectdemo1.domain.boardContent.color.PetColorType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Table(name = "board")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@ToString(exclude = {"comments", "imageSet", "petColor"})

public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bno") //테이블의 컬럼명 (reposrtId에서 변경)
    private Long bno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uno")
    private User user;

    private String title;

    //board1,2 content
    private String petDescription;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lostDate;
    private String location;
    private String postType;
    private String locationDetail;
    private String petBreeds; //품종
    private String petGender;
    private String petAge;
    private String petWeight;
    private String petName;
    private String petType; //동물 종류(개, 고양이 등)


/*    @JoinColumn(name = "petColor")
    @Enumerated(EnumType.STRING)
    @OneToOne(mappedBy = "board")
    private PetColor petColor;*/
    //private PetColorType petColor; //동물 색상

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petColorId") // 컬럼명 설정
    private PetColor petColor;

    private String writer;
    private String content; // board3 content

    @Enumerated(EnumType.STRING)
    private Status status; //게시글 상태

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createdAt")
    @DateTimeFormat(pattern = "yyyy년 MM월 dd일")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updatedAt")
    private LocalDateTime updatedAt;

    @ColumnDefault("0") //기본값 0
    private Long hitCount; //조회수

    public void updateHitcount() {
        this.hitCount =  this.hitCount+1; //조회수 증가
    }

    @ColumnDefault("0")
    private Long replyCount;

    @Enumerated(EnumType.STRING)
    private PetColorType petColorType;


    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("board")
    private List<Comment> comments;

    @PrePersist
    public void prePersist() { //DB에 insert 되기 전에 실행 (조회수, 댓글 수 초기화)
        this.hitCount = this.hitCount == null ? 0 : this.hitCount;
        this.replyCount = this.replyCount == null ? 0 : this.replyCount;
    }


    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = {CascadeType.ALL},
            orphanRemoval = true)
    @BatchSize(size = 50)
    @Builder.Default //윤요섭 쌤 참조

    private Set<BoardImage> imageSet = new HashSet<>();

    public void addImage(String uuid, String fileName) {
        BoardImage image = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(image);
    }

    public void clearImages() {
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));
        this.imageSet.clear();
    }

    //윤쌤 test 코드 위한  메소드
    public void change(String title, String content){
        this.title = title;
        this.content = content;
    }



}
