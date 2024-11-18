package com.projectdemo1.service;

import com.projectdemo1.domain.Post;
import com.projectdemo1.domain.User;
import com.projectdemo1.dto.PageRequestDTO;
import com.projectdemo1.dto.PageResponseDTO;
import com.projectdemo1.dto.PostDTO;
import com.projectdemo1.repository.PostRepository;
import com.projectdemo1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public PageResponseDTO<PostDTO> getPostByUser(User user, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
        Page<Post> result = postRepository.findByUser(user, pageable);
        List<PostDTO> dtoList = result.getContent().stream()
                .map(post -> {
                    PostDTO postDTO = new PostDTO();
                    postDTO.setBno(post.getBno());
                    postDTO.setCno(post.getCno());
                    postDTO.setTitle(post.getTitle());
                    postDTO.setContent(post.getContent());
                    postDTO.setWriter(post.getWriter());
                    postDTO.setCreatedAt(post.getCreatedAt());
                    postDTO.setHitCount(post.getHitCount());
                    return postDTO;
                }).collect(Collectors.toList());
        return new PageResponseDTO<>(dtoList, result.getTotalPages(), result.getTotalElements());

    }
}