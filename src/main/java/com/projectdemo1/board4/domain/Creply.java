package com.projectdemo1.board4.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

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
  /*  @JsonIgnore*/
    private Cboard cboard;


    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    private String replyer; //댓글 작성자

    public void changeText(String text) {
        this.replyText = text;
    }
}
