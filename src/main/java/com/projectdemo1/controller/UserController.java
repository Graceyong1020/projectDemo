package com.projectdemo1.controller;


import com.projectdemo1.domain.User;
import com.projectdemo1.repository.UserRepository;
import com.projectdemo1.service.BoardServiceImpl;

import com.projectdemo1.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2

public class UserController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserServiceImpl userServiceImpl;
    private final BoardServiceImpl boardServiceImpl;

    @GetMapping("user/join")
    public void join() {
    }

    @PostMapping("/user/join")
    public String register(User user, BindingResult bindingResult) {
        System.out.println("register user: " + user);
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        user.setRole("USER"); // USER라는 role을 부여해서 user만 접근 가능하게
        if (bindingResult.hasErrors()) {
            return "user/join";  // 폼 검증 에러가 있으면 회원가입 페이지로 돌아감
        }
        userRepository.save(user);
        return "redirect:/user/login"; // 회원가입 후 로그인 페이지로 이동
    }

    @GetMapping("/user/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/user/edit-profile") // /user/edit-profile로 매핑
    public String editProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // 로그인한 사용자의 username을 통해 DB에서 User 엔티티를 조회
        User user = userRepository.findByUsername(userDetails.getUsername());

        // 조회한 사용자 정보를 모델에 추가
        model.addAttribute("user", user);
        return "user/edit-profile"; // 'edit-profile.html' 파일 경로 반환
    }

    @PostMapping("/user/edit-profile")
    public String editProfile(@AuthenticationPrincipal UserDetails userDetails, Model model, @ModelAttribute User user) {
        // 현재 인증된 사용자의 사용자명 가져오기
        String username = userDetails.getUsername();

        // 해당 사용자 정보를 데이터베이스에서 찾기
        User existingUser = userRepository.findByUsername(username);  // findByUsername 메서드를 통해 사용자 정보 조회

        if (existingUser == null) {
            // 사용자가 존재하지 않으면 처리 (예: 오류 메시지 추가)
            return "redirect:/login";  // 로그인 페이지로 리디렉션 등
        }

        // 비밀번호 인코딩
        String rawPassword = user.getPassword();
        if (rawPassword != null && !rawPassword.isEmpty()) {
            String encPassword = bCryptPasswordEncoder.encode(rawPassword);
            existingUser.setPassword(encPassword);  // 비밀번호 업데이트
        }

        // 다른 수정할 사용자 정보 (이름, 이메일 등) 업데이트
        existingUser.setMobile(user.getMobile());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());

        // 수정된 사용자 정보 저장
        userRepository.save(existingUser);

        // 수정 완료 메시지 (디버깅을 위해 출력)
        System.out.println("수정완료: " + existingUser);

        return "redirect:/user/edit-profile";  // 수정된 프로필 페이지로 리디렉션
    }


    @GetMapping("/home") // 홈화면
    public String home() {
        return "home";
    }

    // "내가 작성한 글 보기" 매핑 추가
/*    @GetMapping("/user/my-posts")
    public String viewMyPosts(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            // username을 사용하여 게시물 불러오기
            PageRequestDTO pageRequestDTO = new PageRequestDTO(); // 필요한 페이지 요청 정보 설정
            PageResponseDTO<BoardDTO> responseDTO = boardServiceImpl.findByUserName(username, pageRequestDTO);

            model.addAttribute("responseDTO", responseDTO); // 결과를 모델에 추가

            return "user/my-posts"; // 정상적으로 게시물 뷰 반환
        } else {
            // 인증되지 않은 사용자 처리
            return "redirect:/login"; // 로그인 페이지로 리디렉션
        }
    }*/




    @GetMapping("/user/delete-account")
    public String deleteAccountPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        model.addAttribute("username", user.getNickname());
        return "user/delete-account";
    }


    @PostMapping("/user/delete-account")
    public String deleteAccount() {
        userServiceImpl.deleteAccount(); // UserServiceImpl의 deleteAccount 메서드를 호출
        return "redirect:/logout"; // 삭제 후 로그아웃으로 리디렉션
    }





    // 아이디 중복 체크 동작 안 됨
    @GetMapping("/check-username")
    @ResponseBody
    public Map<String, Object> checkUserId(@RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        boolean isAvailable = userRepository.findByUsername(username) == null; // 아이디 중복 체크
        response.put("available", isAvailable);
        return response;
    }

    // 닉네임 중복 체크 //동작 안 됨
    @GetMapping("/check-nickname")
    @ResponseBody
    public Map<String, Object> checkNickname(@RequestParam String nickname) {
        Map<String, Object> response = new HashMap<>();
        boolean isAvailable = userRepository.findByNickname(nickname) == null; // 닉네임 중복 체크
        response.put("available", isAvailable);
        return response;
    }
        @GetMapping("/admin/list") // "/user/my-posts"로 매핑
    public String adminList(@AuthenticationPrincipal UserDetails userDetails, Model model) {
            List<User> users = userRepository.findAll();

            // 모든 사용자 정보를 모델에 추가
            model.addAttribute("users", users);

        return "admin/list"; // my-posts.html 템플릿 반환
    }

 /*   @GetMapping("/user/my-posts")
    public String getMyPosts(Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        List<Post> posts = postRepository.findByUser(user);
        model.addAttribute("posts", posts);
        return "user/my-posts";
    }
    private Long getUserFromPrincipal(Principal principal) {
        PrincipalDetails userDetails = (PrincipalDetails) ((Authentication) principal).getPrincipal();
        return userDetails.getUser().getUno();
    }*/
}
