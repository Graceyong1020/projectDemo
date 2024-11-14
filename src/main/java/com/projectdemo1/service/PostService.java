package com.projectdemo1.service;

import com.projectdemo1.domain.Post;
import com.projectdemo1.domain.User;
import com.projectdemo1.dto.PostDTO;
import com.projectdemo1.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<PostDTO> getPostByUser(User user) {
        return postRepository.findByUser(user).stream()
                .map(post -> {
                    PostDTO postDTO = new PostDTO();
                    postDTO.setBno(post.getBno());
                    postDTO.setCno(post.getCno());
                    postDTO.setTitle(post.getTitle());
                    postDTO.setContent(post.getContent());
                    postDTO.setWriter(post.getWriter());
                    postDTO.setCreatedAt(post.getCreatedAt());
                    postDTO.setHitCount(post.getHitCount());
                    postDTO.setPostType(post.getPostType());
                    return postDTO;

                })
                .collect(Collectors.toList());
    }
}
