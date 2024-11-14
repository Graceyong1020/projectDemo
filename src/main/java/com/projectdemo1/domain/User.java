package com.projectdemo1.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
    public static String getMobile;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uno;
    @Column(nullable = false, unique = true)
    private String username; // 계정 아이디
    private String password;
    private String email;
    private String role;
    private String mobile;
    private String nickname;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt")
    @DateTimeFormat(pattern = "yyyy년 MM월 dd일")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Board> boards;

    @Override
    public String toString() {
        return "User{" +
                "uno=" + uno +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", mobile='" + mobile + '\'' +
                ", nickname='" + nickname + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}


