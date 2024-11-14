package com.projectdemo1.repository;

import com.projectdemo1.domain.Post;
import com.projectdemo1.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
}
