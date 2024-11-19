package com.projectdemo1.board4.service;

import com.projectdemo1.board4.domain.Cboard;
import com.projectdemo1.board4.domain.Creply;
import com.projectdemo1.board4.dto.CpageRequestDTO;
import com.projectdemo1.board4.dto.CpageResponseDTO;
import com.projectdemo1.board4.dto.CreplyDTO;
import com.projectdemo1.board4.repository.CboardRepository;
import com.projectdemo1.board4.repository.CreplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Log4j2
@RequiredArgsConstructor
@Service
public class CreplyServiceImpl implements CreplyService{

    private final CreplyRepository creplyRepository;
    private final CboardRepository cboardRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public Long register(CreplyDTO creplyDTO) {
        if (creplyDTO.getCno() == null) {
            throw new IllegalArgumentException("Cno cannot be null");
        }

        Cboard cboard = cboardRepository.findById(creplyDTO.getCno())
                .orElseThrow(() -> new IllegalArgumentException("Cno is not valid"));

        Creply creply = Creply.builder()
                .cboard(cboard)
                .replyText(creplyDTO.getReplyText())
                .replyer(creplyDTO.getReplyer())
                .createdAt(new Date())
                .build();

        if (creplyDTO.getParentRno() != null) {
            Creply parentCreply = creplyRepository.findById(creplyDTO.getParentRno())
                    .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
            creply.setParent(parentCreply);
        }

        //Creply creply = modelMapper.map(creplyDTO, Creply.class);
        creplyRepository.save(creply);
        return creply.getRno();
    }

    @Override
    public CreplyDTO read(Long rno) {
        Optional<Creply> replyOptional = creplyRepository.findById(rno);
        Creply creply = replyOptional.orElseThrow();
        return modelMapper.map(creply, CreplyDTO.class);
    }

    @Override
    public void modify(CreplyDTO creplyDTO) {
        Optional<Creply> replyOptional = creplyRepository.findById(creplyDTO.getRno());
        Creply creply = replyOptional.orElseThrow();
        creply.changeText(creplyDTO.getReplyText());
        creplyRepository.save(creply);

    }

    @Override
    public void remove(Long rno) {
        creplyRepository.deleteById(rno);

    }

    @Override
    public CpageResponseDTO<CreplyDTO> getListOfCboard(Long cno, CpageRequestDTO cpageRequestDTO) {
        Pageable pageable = PageRequest.of(cpageRequestDTO.getPage() <=0? 0: cpageRequestDTO.getPage() -1,
                cpageRequestDTO.getSize(),
                Sort.by("rno").ascending());

        Page<Creply> result = creplyRepository.listOfCboard(cno, pageable);

        List<CreplyDTO> dtoList =
                result.getContent().stream().map(creply -> modelMapper.map(creply, CreplyDTO.class))
                        .collect(Collectors.toList());

        return CpageResponseDTO.<CreplyDTO>withAll()
                .cpageRequestDTO(cpageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }
}
