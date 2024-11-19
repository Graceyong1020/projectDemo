package com.projectdemo1.service;

import com.projectdemo1.dto.CommentDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class ReplyServiceTests {

    @Autowired
    private CommentService replyService;

    @Test
    public void testRegister() {
        CommentDTO commentDTO = CommentDTO.builder()
                //.content("CommentDTO Text")
               // .writer("replyer")
                .bno(100L)
                .build();

        log.info(replyService.register(commentDTO));
    }
}
