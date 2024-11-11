package com.projectdemo1.service;

import com.projectdemo1.domain.Board;
import com.projectdemo1.domain.User;
import com.projectdemo1.domain.boardContent.color.PetColor;
import com.projectdemo1.domain.boardContent.color.PetColorType;
import com.projectdemo1.dto.BoardDTO;
import com.projectdemo1.dto.PageRequestDTO;
import com.projectdemo1.dto.PageResponseDTO;
import com.projectdemo1.repository.BoardRepository;
import com.projectdemo1.repository.PetColorRepository;
import com.projectdemo1.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;
    private final PetColorRepository petColorRepository;

    // 이미 @RequiredArgsConstructor 애너테이션이 생성자를 자동으로 생성하므로, 아래 생성자는 삭제합니다.
    // public BoardServiceImpl(BoardRepository boardRepository, ModelMapper modelMapper, PetColorRepository petColorRepository) {
    //     this.boardRepository = boardRepository;
    //     this.modelMapper = modelMapper;
    //     this.petColorRepository = petColorRepository;
    // }

    @Override
    public void register(Board board, User user) {
        board.setUser(user);
        // PetColor 저장
        PetColor petColor = board.getPetColor();
        if (petColor != null) {
            petColor = petColorRepository.save(petColor); // 먼저 PetColor 저장
        }
        board.setPetColor(petColor);
        boardRepository.save(board);
    }

    @Override
    public List<Board> list() {
        return List.of();
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board, BoardDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    public Board findById(Long bno) {
        Board board = boardRepository.findById(bno).get();
        board.setHitCount(board.getHitCount() + 1);
        boardRepository.save(board);
        return board;
    }

    @Override
    public void modify(Board board) {
        Board b = boardRepository.findById(board.getBno())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        b.setContent(board.getContent());
        b.setTitle(board.getTitle());
        boardRepository.save(b);
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }
    @Override
    public void savePetColor(PetColorType petColorType) {
        // PetColor 저장 로직
        PetColor petColor = new PetColor(petColorType);  // PetColor 객체 생성
        petColorRepository.save(petColor);  // PetColor 저장
    }

}
