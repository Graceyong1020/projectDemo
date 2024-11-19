package com.projectdemo1.board4.controller;


import com.projectdemo1.auth.PrincipalDetails;
import com.projectdemo1.board4.dto.CboardDTO;
import com.projectdemo1.board4.dto.CpageRequestDTO;
import com.projectdemo1.board4.dto.CpageResponseDTO;
import com.projectdemo1.board4.dto.upload.CuploadFileDTO;
import com.projectdemo1.board4.service.CboardService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/cboard")
@Log4j2
@RequiredArgsConstructor
public class CboardController {

    @Value("${com.projectdemo1.board4.upload.path}")
    private String uploadPath; // 업로드 경로

    private final CboardService cboardService;

    @GetMapping("/clist")
    public void list(CpageRequestDTO cpageRequestDTO, Model model) {
        CpageResponseDTO<CboardDTO> cresponseDTO = cboardService.list(cpageRequestDTO);
        log.info(cresponseDTO);
        model.addAttribute("cresponseDTO", cresponseDTO);
        model.addAttribute("cpageRequestDTO", cpageRequestDTO);
    }

    @GetMapping("/cregister") //게시글 등록
    public void registerGET() {
    }

    @PostMapping("/cregister")
    public String registerPOST(CuploadFileDTO cuploadFileDTO, CboardDTO cboardDTO,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes,
                               @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<String> strFileNames = null;
        if (cuploadFileDTO.getFiles() != null && !cuploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")) {
            strFileNames = fileUpload(cuploadFileDTO);
            log.info(strFileNames.size());
        }
        cboardDTO.setFileNames(strFileNames);

        // Set the uno field from the authenticated user
        // cboardDTO.setCno(principalDetails.getUser().getUno());

        log.info("cboard POST register..........");
        log.info((cboardDTO));
        Long cno = cboardService.register(cboardDTO);
        redirectAttributes.addFlashAttribute("result", cno);
        return "redirect:/cboard/clist";
    }

    @GetMapping({"/cread", "/cmodify"})
    public void read(Long cno, CpageRequestDTO cpageRequestDTO, Model model) {
        CboardDTO cboardDTO = cboardService.readOne(cno);
        log.info(cboardDTO);
        model.addAttribute("dto", cboardDTO);
    }

    @PostMapping("/cmodify")
    public String modify(CuploadFileDTO cuploadFileDTO, CpageRequestDTO cpageRequestDTO,
                         @Valid CboardDTO cboardDTO, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        log.info("cboard POST modify.........." + cboardDTO);

        List<String> strFileNames = null;
        if (cuploadFileDTO.getFiles() != null && !cuploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")) {

            List<String> fileNames = cboardDTO.getFileNames();

            if (fileNames != null && fileNames.size() > 0) {
                removeFiles(fileNames);
            }
            strFileNames = fileUpload(cuploadFileDTO);
            log.info(strFileNames.size());
            cboardDTO.setFileNames(strFileNames);
        }
        if (bindingResult.hasErrors()) {
            log.info("has errors");
            String link = cpageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("cno", cboardDTO.getCno());
            return "redirect:/cboard/cmodify?" + link;
        }

        cboardService.modify(cboardDTO);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("cno", cboardDTO.getCno());
        return "redirect:/cboard/cread";

    }

    @PostMapping("/cremove")
    public String remove(CboardDTO cboardDTO, RedirectAttributes redirectAttributes) {
        log.info("cboard POST remove.........." + cboardDTO);

        List<String> fileNames = cboardDTO.getFileNames();
        if (fileNames != null && fileNames.size() > 0) {
            log.info("remove controller" + fileNames.size());
            removeFiles(fileNames);
        }
        cboardService.remove(cboardDTO.getCno());
        redirectAttributes.addFlashAttribute("result", "removed");
        return "redirect:/cboard/clist";
    }

    private List<String> fileUpload(CuploadFileDTO cuploadFileDTO) {
        List<String> list = new ArrayList<>();
        cuploadFileDTO.getFiles().forEach(multipartFile -> {
            String originalName = multipartFile.getOriginalFilename();
            log.info(originalName);

            String uuid = UUID.randomUUID().toString();
            Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);
            boolean image = false;
            try {
                multipartFile.transferTo(savePath);
                if (Files.probeContentType(savePath).startsWith("image")) {
                    image = true;
                    // 썸네일 생성
                    File thumbnail = new File(uploadPath, "s_" + uuid + "_" + originalName);
                    Thumbnailator.createThumbnail(savePath.toFile(), thumbnail, 100, 100);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            list.add(uuid + "_" + originalName);
        });
        return list;
    }

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

    private void removeFiles(List<String> fileNames) {
        log.info(fileNames.size());

        for (String fileName : fileNames) {
            log.info(fileName);
            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
            String resourceName = resource.getFilename();

            boolean removed = false;

            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());
                removed = resource.getFile().delete();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }


    }


}

