package com.projectdemo1.board4.service;


import com.projectdemo1.board4.domain.Cboard;
import com.projectdemo1.board4.dto.CboardDTO;
import com.projectdemo1.board4.dto.CpageRequestDTO;
import com.projectdemo1.board4.dto.CpageResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface CboardService {
    Long register(CboardDTO cboardDTO);
    CboardDTO readOne(Long cno);
    void modify(CboardDTO cboardDTO);
    void remove(Long cno);
    CpageResponseDTO<CboardDTO> list(CpageRequestDTO cpageRequestDTO);

    default Cboard dtoToEntity(CboardDTO cboardDTO) {
        Cboard cboard = Cboard.builder()
                .cno(cboardDTO.getCno())
                .title(cboardDTO.getTitle())
                .content(cboardDTO.getContent())
                .writer(cboardDTO.getWriter())
                .build();
        if(cboardDTO.getFileNames()!=null){
            cboardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                cboard.addImage(arr[0], arr[1]);
            });
        }
        return cboard;
    }
    default CboardDTO entityToDTO(Cboard cboard) {
        CboardDTO cboardDTO = CboardDTO.builder()
                .cno(cboard.getCno())
                .title(cboard.getTitle())
                .content(cboard.getContent())
                .writer(cboard.getWriter())
                .createdAt(cboard.getCreatedAt())
                .updatedAt(cboard.getUpdatedAt())
                .build();

        List<String> fileNames = cboard.getImageSet().stream()
                .sorted().map(cboardImage -> cboardImage.getUuid()+"_"+cboardImage.getFileName())
                .collect(Collectors.toList());
        cboardDTO.setFileNames(fileNames);
        return cboardDTO;
    }
}