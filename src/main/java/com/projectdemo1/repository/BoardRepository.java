package com.projectdemo1.repository;

import com.projectdemo1.domain.Board;
import com.projectdemo1.domain.User;
import com.projectdemo1.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select b from Board b where b.bno = :bno")
    Optional<Board> findByIdWithImages(Long bno);

    // User와 관련된 Board 엔티티 삭제 메서드 선언
    void deleteByUser(User user);
    Page<Board> findByUser_Username(String username, Pageable pageable);
}
