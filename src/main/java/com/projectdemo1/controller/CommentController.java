package com.projectdemo1.controller;


import com.projectdemo1.dto.CommentDTO;
import com.projectdemo1.dto.PageRequestDTO;
import com.projectdemo1.dto.PageResponseDTO;
import com.projectdemo1.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commetService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(
            @Valid @RequestBody CommentDTO commentDTO, BindingResult bindingResult) throws BindException {

        log.info("check" + commentDTO);

        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        if (commentDTO.getBno()==null) {
            throw new IllegalArgumentException("Bno is null");
        }

        Map<String, Long> resultMap = new HashMap<>();
        Long rno = commetService.register(commentDTO);
        resultMap.put("rno", rno);
        return resultMap;
    }

    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<CommentDTO> getList(@PathVariable("bno") Long bno, PageRequestDTO pagerequestDTO) {
        PageResponseDTO<CommentDTO> responseDTO = commetService.getListOfBoard(bno, pagerequestDTO);
        return responseDTO;
    }

    @GetMapping("/{rno}")
    public CommentDTO getCommentDTO(@PathVariable("rno") Long rno) {
        CommentDTO commentDTO = commetService.read(rno);
        return commentDTO;
    }

    @DeleteMapping("/{rno}")
    public Map<String, Long> remove(@PathVariable("rno") Long rno) {
        commetService.remove(rno);
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);
        return resultMap;
    }

    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> remove(@PathVariable("rno") Long rno, @RequestBody CommentDTO commentDTO) {
        commentDTO.setRno(rno);
        commetService.modify(commentDTO);
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);
        return resultMap;
    }




}

