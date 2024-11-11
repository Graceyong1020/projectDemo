package com.projectdemo1.controller; //  박경미 쌤 코드
import com.projectdemo1.auth.PrincipalDetails;
import com.projectdemo1.board4.domain.Cboard;
import com.projectdemo1.board4.dto.CboardDTO;
import com.projectdemo1.board4.dto.CpageRequestDTO;
import com.projectdemo1.board4.dto.CpageResponseDTO;
import com.projectdemo1.domain.Board;
import com.projectdemo1.domain.User;
import com.projectdemo1.domain.boardContent.color.PetColor;
import com.projectdemo1.domain.boardContent.color.PetColorType;
import com.projectdemo1.dto.BoardDTO;
import com.projectdemo1.dto.PageRequestDTO;
import com.projectdemo1.dto.PageResponseDTO;
import com.projectdemo1.dto.upload.UploadFileDTO;
import com.projectdemo1.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
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

   
    private List<String> fileUload(UploadFileDTO uploadFileDTO){

        List<String> list = new ArrayList<>();
        uploadFileDTO.getFiles().forEach(multipartFile -> {
            String originalName = multipartFile.getOriginalFilename();
            log.info(originalName);

            String uuid = UUID.randomUUID().toString();
            Path savePath = Paths.get(uploadPath, uuid+"_"+ originalName);
            boolean image = false;
            try {
                multipartFile.transferTo(savePath); // 서버에 파일저장
                //이미지 파일의 종류라면
                if(Files.probeContentType(savePath).startsWith("image")){
                    image = true;
                    File thumbFile = new File(uploadPath, "s_" + uuid+"_"+ originalName);
                    Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200,200);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            list.add(uuid+"_"+originalName);
        });
        return list;
    }

    @GetMapping("/register") //게시글 등록
    public String register() {
        return "/board/register";
    }


    @PostMapping("/register")
    public String register(@Valid @ModelAttribute Board board,
                           @RequestParam PetColorType petColorType,  // PetColorType을 URL 쿼리 파라미터로 받음
                           PrincipalDetails principal) {
        PetColor petColor = new PetColor(petColorType);  // PetColorType을 사용하여 PetColor 객체 생성
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
    @GetMapping("/read")
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


//    @GetMapping("/modify")
//    public String modify(@RequestParam Long bno, Model model) {
//        model.addAttribute("board", boardService.findById(bno));
//        return "board/modify";
//    }

    @PostMapping("/modify")
    public String modify(Board board, @RequestParam("petColorType") PetColorType petColorType) {
        System.out.println(board);
        PetColor petColor = new PetColor(petColorType);
       // petColor.setColor(petColorType);
        board.setPetColor(petColor);  // Board에 PetColor 설정
        boardService.modify(board);
        return "redirect:/board/read?bno=" + board.getBno();
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
        Board board = boardService.findById(id);
        model.addAttribute("board", board);
        model.addAttribute("postType", board.getPostType()); // 추가된 필드 사용

        return "board/list"; // 템플릿 이름
    }


    @GetMapping("/view/{fileName}")
    @ResponseBody
    public ResponseEntity<Resource> viewFileGet(@PathVariable("fileName") String fileName){
        Resource resource = new FileSystemResource(uploadPath+File.separator + fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try{
            headers.add("Content-Type", Files.probeContentType( resource.getFile().toPath() ));
        } catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    private void removeFile(List<String> fileNames){
        log.info("AAAAA"+fileNames.size());

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
/*
package com.projectdemo1.controller;

import com.projectdemo1.dto.BoardDTO;
import com.projectdemo1.dto.BoardListReplyCountDTO;
import com.projectdemo1.dto.PageRequestDTO;
import com.projectdemo1.dto.PageResponseDTO;
import com.projectdemo1.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        // PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        PageResponseDTO<BoardListReplyCountDTO> responseDTO = boardService.listWithReplyCount(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);

    }

    @GetMapping("/register")
    public void registerGET(){

    }

    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        log.info("board POST register.......");

        if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            return "redirect:/board/register";
        }

        log.info(boardDTO);

        Long bno  = boardService.register(boardDTO);

        redirectAttributes.addFlashAttribute("result", bno);

        return "redirect:/board/list";
    }


//    @GetMapping("/read")
//    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model){
//
//        BoardDTO boardDTO = boardService.readOne(bno);
//
//        log.info(boardDTO);
//
//        model.addAttribute("dto", boardDTO);
//
//    }


    @GetMapping({"/read", "/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model){

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);

    }

    @PostMapping("/modify")
    public String modify( PageRequestDTO pageRequestDTO,
                          @Valid BoardDTO boardDTO,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes){

        log.info("board modify post......." + boardDTO);

        if(bindingResult.hasErrors()) {
            log.info("has errors.......");

            String link = pageRequestDTO.getLink();

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );

            redirectAttributes.addAttribute("bno", boardDTO.getBno());

            return "redirect:/board/modify?"+link;
        }

        boardService.modify(boardDTO);

        redirectAttributes.addFlashAttribute("result", "modified");

        redirectAttributes.addAttribute("bno", boardDTO.getBno());

        return "redirect:/board/read";
    }


    @PostMapping("/remove")
    public String remove(Long bno, RedirectAttributes redirectAttributes) {

        log.info("remove post.. " + bno);

        boardService.remove(bno);

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/board/list";

    }
}
*/