package com.projectdemo1.controller;/*
package com.projectdemo1.controller;

import com.projectdemo1.domain.Comment;
import com.projectdemo1.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/list")
    public String list() {
        return "admin/adminboard.html"; // HTML 파일의 위치에 따라 변경
    }
    // 댓글 목록 조회
    @GetMapping("/comments/{bno}")
    @ResponseBody
    public List<Comment> listComments(@PathVariable Long bno) {
        return commentService.listByBoard(bno);

    }

    // 댓글 등록
    @PostMapping("/comments")
    @ResponseBody
    public void registerComment(@RequestBody Comment comment) {
        commentService.register(comment);
    }

    // 댓글 수정
    @PutMapping("/comments/{cno}")
    @ResponseBody
    public void modifyComment(@PathVariable Long cno, @RequestBody Comment comment) {
        comment.setCno(cno); // 댓글 ID 설정
        commentService.modify(comment);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{cno}")
    @ResponseBody
    public void removeComment(@PathVariable Long cno) {
        commentService.remove(cno);
    }
}
*/
