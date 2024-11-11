package com.projectdemo1.service;


import com.projectdemo1.board4.domain.Cboard;
import com.projectdemo1.board4.dto.CboardDTO;
import com.projectdemo1.board4.dto.CpageRequestDTO;
import com.projectdemo1.board4.dto.CpageResponseDTO;
import com.projectdemo1.domain.Board;
import com.projectdemo1.domain.User;
import com.projectdemo1.domain.boardContent.color.PetColor;
import com.projectdemo1.domain.boardContent.color.PetColorType;
import com.projectdemo1.dto.BoardDTO;
import com.projectdemo1.dto.BoardListReplyCountDTO;
import com.projectdemo1.dto.PageRequestDTO;
import com.projectdemo1.dto.PageResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {

void register(Board board, User user);
    List<Board> list();
    Board findById(Long bno);
    void modify(Board board);
    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    default Board dtoToEntity(BoardDTO boardDTO) {
        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .build();
        if(boardDTO.getFileNames()!=null){
            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            });
        }
        return board;
    }
    default BoardDTO entityToDTO(Board board) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .postType(board.getPostType())  // 추가된 필드들
                .hitCount(board.getHitCount())
                .status(board.getStatus())
                .petDescription(board.getPetDescription())
                .lostDate(board.getLostDate())
                .lostLocation(board.getLostLocation())
                .petBreeds(board.getPetBreeds())
                .petGender(board.getPetGender())
                .petAge(board.getPetAge())
                .petWeight(board.getPetWeight())
                .petType(board.getPetType())
                .mobile(User.getMobile)
                .build();

        // 파일 이름 처리
        List<String> fileNames = board.getImageSet().stream()
                .sorted()
                .map(boardImage -> boardImage.getUuid() + "_" + boardImage.getFileName())
                .collect(Collectors.toList());
        boardDTO.setFileNames(fileNames);

        // User 객체에서 mobile과 email 값 가져오기
        if (board.getUser() != null) {
            boardDTO.setMobile(board.getUser().getMobile());  // User의 mobile 값
            boardDTO.setEmail(board.getUser().getEmail());    // User의 email 값
        }

        return boardDTO;
    }

    void savePetColor(PetColorType petColorType);
}
