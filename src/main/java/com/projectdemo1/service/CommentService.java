package com.projectdemo1.service;

import com.projectdemo1.dto.CommentDTO;
import com.projectdemo1.dto.PageRequestDTO;
import com.projectdemo1.dto.PageResponseDTO;

public interface CommentService {

    Long register(CommentDTO dto);

    CommentDTO read(Long rno);


    void modify(CommentDTO dto);

    void remove(Long rno);

    PageResponseDTO<CommentDTO> getListOfBoard(Long bno, PageRequestDTO requestDTO);

}
