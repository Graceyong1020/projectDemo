package com.projectdemo1.service;

import com.projectdemo1.domain.Board;
import com.projectdemo1.domain.Comment;
import com.projectdemo1.dto.CommentDTO;
import com.projectdemo1.dto.PageRequestDTO;
import com.projectdemo1.dto.PageResponseDTO;
import com.projectdemo1.repository.BoardRepository;
import com.projectdemo1.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long register(CommentDTO commentDTO) {
        Comment comment =Comment.builder()
                .replyText(commentDTO.getReplyText())
                .replyer(commentDTO.getReplyer())
                .build();
        Board board = boardRepository.getOne(commentDTO.getBno());
        comment.setBoard(board);

        commentRepository.save(comment);
        return comment.getRno();
    }

    @Override
    public CommentDTO read(Long rno) {
        Optional<Comment> commentOptional = commentRepository.findById(rno);
        Comment comment = commentOptional.orElseThrow();
        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public void modify(CommentDTO commentDTO) {
        Optional<Comment> commentOptional = commentRepository.findById(commentDTO.getRno());
        Comment comment= commentOptional.orElseThrow();
        comment.changeText(commentDTO.getReplyText());
        commentRepository.save(comment);

    }

    @Override
    public void remove(Long rno) {
        commentRepository.deleteById(rno);

    }

    @Override
    public PageResponseDTO<CommentDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <=0? 0: pageRequestDTO.getPage() -1,
                pageRequestDTO.getSize(),
                Sort.by("rno").ascending());

        Page<Comment> result = commentRepository.listOfBoard(bno, pageable);

        List<CommentDTO> dtoList =
                result.getContent().stream().map(comment -> modelMapper.map(comment, CommentDTO.class))
                        .collect(Collectors.toList());

        return PageResponseDTO.<CommentDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }
}

