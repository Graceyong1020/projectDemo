package com.projectdemo1.service;

import com.projectdemo1.domain.Board;
import com.projectdemo1.domain.User;
import com.projectdemo1.dto.BoardDTO;
import com.projectdemo1.dto.PageRequestDTO;
import com.projectdemo1.dto.PageResponseDTO;
import com.projectdemo1.repository.BoardRepository;
import com.projectdemo1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void deleteAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            // 사용자 이름을 이용해 계정 삭제 처리
            User user = userRepository.findByUsername(username);
            if (user != null) {
                // 계정 삭제
                userRepository.delete(user);
            } else {
                throw new IllegalStateException("사용자를 찾을 수 없습니다.");
            }
        } else {
            throw new IllegalStateException("사용자가 인증되지 않았습니다.");
        }
    }

    @Transactional
    public void deleteAccountWithDependencies(User user) {
        // 해당 사용자와 관련된 데이터를 모두 삭제 (예: board)
        boardRepository.deleteByUser(user); // boardRepository에 이 메서드가 필요함
        userRepository.delete(user);
    }


    public void saveUser(User user){
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        user.setRole("USER");
        userRepository.save(user);
    }

    @Override
    public void register(Board board, User user) {

    }

    @Override
    public List<Board> list() {
        return List.of();
    }

    @Override
    public BoardDTO findById(Long bno) {
        return null;
    }

    @Override
    public void modify(BoardDTO boardDTO) {

    }

    @Override
    public void remove(Long bno) {

    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        return null;
    }

}
