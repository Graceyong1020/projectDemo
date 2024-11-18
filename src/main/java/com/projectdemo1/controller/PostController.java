package com.projectdemo1.controller;

import com.projectdemo1.domain.User;
import com.projectdemo1.dto.PageRequestDTO;
import com.projectdemo1.dto.PageResponseDTO;
import com.projectdemo1.dto.PostDTO;
import com.projectdemo1.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostController {
    @Autowired
    private PostService postService;


    @GetMapping("/user/my-posts")
    public String viewMyPosts(@AuthenticationPrincipal UserDetails userDetails, PageRequestDTO pageRequestDTO, Model model) {
        String username = userDetails.getUsername();
        User user = postService.findUserByUsername(username);
        PageResponseDTO<PostDTO> responseDTO = postService.getPostByUser(user, pageRequestDTO);
        model.addAttribute("posts", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        return "user/my-posts";
    }
}
