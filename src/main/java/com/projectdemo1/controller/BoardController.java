package com.projectdemo1.controller; //  박경미 쌤 코드

import com.projectdemo1.auth.PrincipalDetails;
import com.projectdemo1.board4.dto.CpageRequestDTO;
import com.projectdemo1.domain.Board;
import com.projectdemo1.domain.User;
import com.projectdemo1.domain.boardContent.color.PetColor;
import com.projectdemo1.domain.boardContent.color.PetColorType;
import com.projectdemo1.dto.BoardDTO;
import com.projectdemo1.dto.PageRequestDTO;
import com.projectdemo1.dto.PageResponseDTO;
import com.projectdemo1.dto.upload.UploadFileDTO;
import com.projectdemo1.repository.UserRepository;
import com.projectdemo1.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    @Value("${com.projectdemo1.board4.upload.path}")
    private String uploadPath;

    private final BoardService boardService;
    private final UserRepository userRepository;



    private List<String> fileUload(UploadFileDTO uploadFileDTO) {

        List<String> list = new ArrayList<>();
        uploadFileDTO.getFiles().forEach(multipartFile -> {
            String originalName = multipartFile.getOriginalFilename();
            log.info(originalName);

            String uuid = UUID.randomUUID().toString();
            Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);
            boolean image = false;
            try {
                multipartFile.transferTo(savePath); // 서버에 파일저장
                //이미지 파일의 종류라면
                if (Files.probeContentType(savePath).startsWith("image")) {
                    image = true;
                    File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalName);
                    Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            list.add(uuid + "_" + originalName);
        });
        return list;
    }

    @GetMapping("/register") //게시글 등록
    public void register() {
    }



    @PostMapping("/register")
    public String register(@Valid @ModelAttribute Board board,
                           @RequestParam PetColorType petColorType,  // PetColorType을 URL 쿼리 파라미터로 받음
                           UploadFileDTO uploadFileDTO, BoardDTO boardDTO,
                           RedirectAttributes redirectAttributes, BindingResult bindingResult,
                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        PetColor petColor = new PetColor(petColorType);  // PetColorType을 사용하여 PetColor 객체 생성
        board.setPetColor(petColor);
        boardService.register(board, principalDetails.getUser());

        log.info("boardDTO: " + boardDTO);
        List<String> strFileNames = null;
        if (uploadFileDTO.getFiles() != null && !uploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")) {
            strFileNames = fileUload(uploadFileDTO);
            log.info(strFileNames.size());
        }
        boardDTO.setFileNames(strFileNames);
        boardDTO.setBno(principalDetails.getUser().getUno());

        log.info("board POST register.........." + boardDTO);
        log.info((boardDTO));
        Long bno = boardService.register(boardDTO);
        redirectAttributes.addFlashAttribute("result", bno);
        return "redirect:/board/list";
    }
  /*  @PostMapping("/register")
    public String register(@Valid @ModelAttribute Board board,
                           @RequestParam PetColorType petColorType,  // PetColorType을 URL 쿼리 파라미터로 받음
                           PrincipalDetails principal) {
        PetColor petColor = new PetColor(petColorType);  // PetColorType을 사용하여 PetColor 객체 생성
        board.setPetColor(petColor);
        boardService.register(board, principal.getUser());

        return "redirect:/board/list";
    }*/


    @GetMapping("/register1")
    public String register1() {
        return "board/register1";
    }

    @PostMapping("/register1")
    public String register1(@Valid @ModelAttribute Board board,
                            @RequestParam PetColorType petColorType,  // PetColorType as a request parameter
                            PrincipalDetails principal) {
        PetColor petColor = new PetColor(petColorType);  // Create PetColor object using PetColorType
        board.setPetColor(petColor);
        boardService.register(board, principal.getUser());

        return "redirect:/board/list";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(PetColor.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    PetColor petColor = new PetColor(PetColorType.valueOf(text));
                    setValue(petColor);
                } catch (IllegalArgumentException e) {
                    // 예외 처리 로직 추가 (예: 기본 색상 설정)
                    PetColor petColor = new PetColor(PetColorType.OTHER);  // 기본값 설정
                    setValue(petColor);
                }
            }
        });
    }

    /*@GetMapping("/read")
    public String read(@RequestParam("bno") Long bno, Model model) {
        Board board = boardService.findById(bno);  // Board 엔티티 조회
        BoardDTO dto = new BoardDTO(board);  // BoardDTO로 변환

        // 기본값 설정 - PetColor 객체 생성 후 PetColorType 설정
        if (dto.getPetColor() == null) {
            dto.setPetColor(new PetColor(PetColorType.OTHER)); // 기본값을 PetColor 객체로 설정
        }

        // user 객체가 null이면 기본값 설정
        if (dto.getUser() == null) {
            dto.setUser(new User());  // 기본 User 객체 설정
        }

        model.addAttribute("dto", dto);
        return "board/read";  // "board/read" 템플릿을 반환
    }
    @GetMapping("/modify")
    public String modify(@RequestParam Long bno, Model model) {
        model.addAttribute("board", boardService.findById(bno));
        return "board/modify";
    }

    @PostMapping("/modify")
    public String modify(Board board, @RequestParam("petColorType") PetColorType petColorType) {
        System.out.println(board);
        PetColor petColor = new PetColor(petColorType);
        // petColor.setColor(petColorType);
        board.setPetColor(petColor);  // Board에 PetColor 설정
        boardService.modify(board);
        return "redirect:/board/read?bno=" + board.getBno();
    }
*/


    @GetMapping({"/read", "/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model) {
        BoardDTO boardDTO = boardService.findById(bno);

        if (boardDTO.getUser() == null) {
            User defaultUser = new User();
            defaultUser.setNickname("Default Nickname"); // Set a default nickname
            boardDTO.setUser(defaultUser);
        }


        log.info(boardDTO);
        model.addAttribute("dto", boardDTO);

    }


    @PostMapping("/modify")
    public String modify(UploadFileDTO uploadFileDTO, CpageRequestDTO pageRequestDTO,
                         @Valid BoardDTO boardDTO, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        log.info("board POST modify.........." + boardDTO);

        List<String> strFileNames = null;
        if (uploadFileDTO.getFiles() != null && !uploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")) {

            List<String> fileNames = boardDTO.getFileNames();

            if (fileNames != null && fileNames.size() > 0) {
                removeFile(fileNames);
            }
            strFileNames = fileUload(uploadFileDTO);
            log.info(strFileNames.size());
            boardDTO.setFileNames(strFileNames);
        }
        if (bindingResult.hasErrors()) {
            log.info("has errors");
            String link = pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bno", boardDTO.getBno());
            return "redirect:/board/modify?" + link;
        }
        boardService.modify(boardDTO);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/board/read";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        List<Board> lists = boardService.list();
        model.addAttribute("lists", lists);
        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        log.info(responseDTO);
        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
    }

    @PostMapping("/remove")
    public String remove(@RequestParam Long bno) {
        boardService.remove(bno);
        return "redirect:/board/list";
    }

    @GetMapping("/board/{id}")
    public String getBoard(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO); // 추가된 필드 사용
        model.addAttribute("postType", boardDTO.getPostType()); // 추가된 필드 사용

        return "board/list"; // 템플릿 이름
    }
  /*  @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, Model model) {
        Board board = boardService.findById(id);
        model.addAttribute("board", board);
        return "board/edit"; // 수정 페이지로 이동하는 템플릿 이름
    } 아마 필요 없을 것*/

    // @GetMapping을 통해 삭제 요청을 GET 방식으로 처리하도록 수정
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boardService.remove(id);  // id에 해당하는 게시글을 삭제
        redirectAttributes.addFlashAttribute("result", "removed");
        return "redirect:/board/list";  // 삭제 후 게시글 목록 페이지로 리다이렉트
    }

    /*@GetMapping("/view/{fileName}")
    @ResponseBody
    public ResponseEntity<Resource> viewFileGet(@PathVariable("fileName") String fileName) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    private void removeFile(List<String> fileNames) {
        log.info("----------" + fileNames.size());

        for (String fileName : fileNames) {
            log.info("fileRemove method: " + fileName);
            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
            String resourceName = resource.getFilename();

            // Map<String, Boolean> resultMap = new HashMap<>();
            boolean removed = false;

            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());
                removed = resource.getFile().delete();

                //섬네일이 존재한다면
                if (contentType.startsWith("image")) {
                    String fileName1 = fileName.replace("s_", "");
                    File originalFile = new File(uploadPath + File.separator + fileName1);
                    originalFile.delete();
                }

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }*/
    @GetMapping("/view/{fileName}")
    @ResponseBody
    public ResponseEntity<Resource> viewFileGet(@PathVariable("fileName") String fileName) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    private void removeFile(List<String> fileNames){
        log.info("-----------------"+fileNames.size());

        for(String fileName:fileNames){
            log.info("fileRemove method: "+fileName);
            Resource resource = new FileSystemResource(uploadPath+File.separator + fileName);
            String resourceName = resource.getFilename();

            // Map<String, Boolean> resultMap = new HashMap<>();
            boolean removed = false;

            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());
                removed = resource.getFile().delete();

                //섬네일이 존재한다면
                if(contentType.startsWith("image")){
                    String fileName1=fileName.replace("s_","");
                    File originalFile = new File(uploadPath+File.separator + fileName1);
                    originalFile.delete();
                }

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}