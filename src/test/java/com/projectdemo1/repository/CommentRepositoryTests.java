package com.projectdemo1.repository;

import com.projectdemo1.domain.Board;
import com.projectdemo1.domain.Comment;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;



@SpringBootTest
@Log4j2
public class CommentRepositoryTests {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testInsert() {
        //실제 DB에 있는 bno
        Long bno = 100L;

        Board board = Board.builder().bno(bno).build();

        Comment comment = Comment.builder()
                .board(board)
                //.content("댓글....")
               // .writer("replyer1")
                .build();

        commentRepository.save(comment);
    }

    @Transactional
    @Test
    public void testBoardReplies() {
        Long bno = 100L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());

        Page<Comment> result = commentRepository.listOfBoard(bno, pageable);
        result.getContent().forEach(comment -> {
            log.info(comment);

        });
    }
}
