package com.projectdemo1.domain;


import jakarta.persistence.*;
import lombok.*;

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
}
