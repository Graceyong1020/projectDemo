package com.projectdemo1.board4.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "cboard")
@Entity
@Table(name = "community_reply", indexes = {@Index(name = "idx_cboard_cno", columnList = "cno")})
public class Creply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;
    private String replyText;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cno", nullable = false)
    @JsonIgnore
    private Cboard cboard;


    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    private String replyer; //댓글 작성자

    public void changeText(String text) {
        this.replyText = text;
    }


    //대댓글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_rno")
    @JsonIgnore
    private Creply parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Creply> children = new ArrayList<>();

}
