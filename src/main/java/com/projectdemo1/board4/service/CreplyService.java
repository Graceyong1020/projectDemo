package com.projectdemo1.board4.service;



import com.projectdemo1.board4.dto.CpageRequestDTO;
import com.projectdemo1.board4.dto.CpageResponseDTO;
import com.projectdemo1.board4.dto.CreplyDTO;

public interface CreplyService {

    Long register(CreplyDTO creplyDTO);

    CreplyDTO read(Long rno);


    void modify(CreplyDTO dto);

    void remove(Long rno);

    CpageResponseDTO<CreplyDTO> getListOfCboard(Long cno, CpageRequestDTO requestDTO);

}
