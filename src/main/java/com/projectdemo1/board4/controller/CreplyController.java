package com.projectdemo1.board4.controller;


import com.projectdemo1.board4.dto.CpageRequestDTO;
import com.projectdemo1.board4.dto.CpageResponseDTO;
import com.projectdemo1.board4.dto.CreplyDTO;
import com.projectdemo1.board4.service.CreplyService;
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
@RequestMapping("/creplies")
@Log4j2
@RequiredArgsConstructor
public class CreplyController {
    private final CreplyService creplyService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(
            @Valid @RequestBody CreplyDTO creplyDTO, BindingResult bindingResult) throws BindException {

        log.info("check" + creplyDTO);

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        if (creplyDTO.getCno() == null) {
            throw new IllegalArgumentException("Cno is null");
        }

        Map<String, Long> resultMap = new HashMap<>();
        Long rno = creplyService.register(creplyDTO); // change to cno
        resultMap.put("rno", rno);
        return resultMap;
    }

    @GetMapping(value = "/clist/{cno}")
    public CpageResponseDTO<CreplyDTO> getList(@PathVariable("cno") Long cno, CpageRequestDTO cpagerequestDTO) {
        CpageResponseDTO<CreplyDTO> cresponseDTO = creplyService.getListOfCboard(cno, cpagerequestDTO);
        return cresponseDTO;
    }

    @GetMapping("/{rno}")
    public CreplyDTO getCreplyDTO(@PathVariable("rno") Long rno) {
        CreplyDTO creplyDTO = creplyService.read(rno);
        return creplyDTO;
    }

    @DeleteMapping("/{rno}")
    public Map<String, Long> remove(@PathVariable("rno") Long rno) {
        creplyService.remove(rno);
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);
        return resultMap;
    }

    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> modify (@PathVariable("rno") Long rno, @RequestBody CreplyDTO creplyDTO) {
        creplyDTO.setRno(rno);
        creplyService.modify(creplyDTO);
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);
        return resultMap;
    }


}
