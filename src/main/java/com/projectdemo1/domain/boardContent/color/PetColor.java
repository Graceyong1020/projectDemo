package com.projectdemo1.domain.boardContent.color;

import com.projectdemo1.domain.Board;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "board")
@Entity
public class PetColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "colorId")
    private Long bno; // 이 필드는 식별자로, Board와의 관계에서 보통 `colorId`로 사용될 수 있음

    @Enumerated(EnumType.STRING)
    private PetColorType color; // enum으로 색상 설정

    @OneToOne(mappedBy = "petColor", fetch = FetchType.LAZY)
    private Board board;

    public PetColor() {} // 기본 생성자

    public PetColor(PetColorType color) {
        this.color = color;
    }
}
