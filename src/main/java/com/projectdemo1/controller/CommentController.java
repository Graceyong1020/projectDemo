package com.projectdemo1.controller;


import com.projectdemo1.domain.Comment;
import com.projectdemo1.dto.CommentDTO;
import com.projectdemo1.dto.PageResponseDTO;
import io.swagger.annotations.ApiOperation;

import com.projectdemo1.dto.PageRequestDTO;
import com.projectdemo1.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

  /* 박경미 선생님 코드 참조
   @PostMapping("/{boardId}")
    public ResponseEntity<Long> createComment(
            @PathVariable Long boardId,
            @RequestBody CommentDTO commentDTO
    ) {
        Comment comment = commentService.createComment(boardId, commentDTO);
        return ResponseEntity.ok(comment.getRno());
    }
   // @GetMapping("/{boardId}")
    @GetMapping("/{bno}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long boardId) {
        List<Comment> comments = commentService.getComments(boardId);
        return ResponseEntity.ok(comments);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> removeComment(@PathVariable("id") Long id){
        commentService.deleteComment(id);
        return ResponseEntity.ok(id);
    }*/
/*윤요섭 쌤 코드 참조*/
    @ApiOperation(value = "Replies POST", notes = "POST 방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    //  public ResponseEntity<Map<String, Long>> register(@Valid @RequestBody CommentDTO commentDTO,
    //                                                   BindingResult bindingResult) throws BindException {
    public Map<String, Long> register(@Valid @RequestBody CommentDTO commentDTO,
                                      BindingResult bindingResult) throws BindException {

        log.info(commentDTO);

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        //Map<String, Long> resultMap = new HashMap<>();
        //resultMap.put("rno", 111L);

        Map<String, Long> resultMap = new HashMap<>();

        Long rno = commentService.register(commentDTO);
        resultMap.put("rno", rno);

        //Map<String, Long> resultMap = Map.of("rno",111L);

        //  return ResponseEntity.ok(resultMap);
        return resultMap;
    }
    @ApiOperation(value = "Replies of Board", notes ="GET 방식으로 특정 게시물의 댓글 목록")
    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<CommentDTO> getList(@PathVariable("bno") Long bno,
                                               PageRequestDTO pageRequestDTO){
        PageResponseDTO<CommentDTO> responseDTO = commentService.getListOfBoard(bno, pageRequestDTO);

        return responseDTO;
    }
    @ApiOperation(value = "Read Comment", notes ="GET 방식으로 특정 댓글 조회")
    @GetMapping(value = "/{rno}")
    public CommentDTO getCommentDTO (@PathVariable("rno") Long rno){
        CommentDTO commentDTO = commentService.read(rno);
        return commentDTO;
    }

    @ApiOperation(value = "Delete Comment", notes ="DELETE 방식으로 특정 댓글 삭제")
    @DeleteMapping("/{rno}")
    public Map<String, Long> remove (@PathVariable("rno") Long rno){
        commentService.remove(rno);

        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);
        return resultMap;
    }

    @ApiOperation(value = "Modify Comment", notes ="PUT 방식으로 특정 댓글 수정")
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> modify (@PathVariable("rno") Long rno, @RequestBody CommentDTO commentDTO) { //remove -> modify로 수정
        commentDTO.setRno(rno); //번호를 일치시킴

        commentService.modify(commentDTO);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);
        return resultMap;
    }
}